package view;

import java.util.concurrent.TimeUnit;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

import com.dataartisans.data.KeyedDataPoint;
import com.dataartisans.sinks.InfluxDBSink;

import controller.CSVMapFunktion;

public class CSVFileReaderView {

	public static void main(String[] args) throws Exception {

		// Checking input parameters
		final ParameterTool params = ParameterTool.fromArgs(args);

		// set up the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// time stamp is set by sensors at the time of the event
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		// boiler plate for this demo
		env.setRestartStrategy(RestartStrategies.fixedDelayRestart(1000, 1000));
		// env.setParallelism(1);
		env.disableOperatorChaining();

		// make parameters available in the web interface
		env.getConfig().setGlobalJobParameters(params);

		// read the text file from given input path
		DataStream<String> textData = env.readTextFile(params.get("input"));

		// position in line starting with zero
		final int TIMESTAMP_POSITION = 3;
		final int VALUE_POSITION = 2;

		// original data
		SingleOutputStreamOperator<KeyedDataPoint<Double>> dataStream = textData
				.map(new CSVMapFunktion(TIMESTAMP_POSITION, VALUE_POSITION)).name("csv data stream");

		SingleOutputStreamOperator<KeyedDataPoint<Double>> movingAverageStream = dataStream
				.map(new MovingAverageMapFunction());

		AllWindowedStream<KeyedDataPoint<Double>, GlobalWindow> windowedStream = dataStream.countWindowAll(2, 1);
		// .allowedLateness(Time.milliseconds(10));

		SingleOutputStreamOperator<KeyedDataPoint<Double>> autoregressivStream = windowedStream
				.reduce(new AutoregessiveReduceFunction());

		// Write this sensor stream out to InfluxDB
		SinkFunction<KeyedDataPoint<Double>> sinkFunction = new InfluxDBSink<>("csvFile");

		// Combine all the streams into one and write it to Kafka
		dataStream.union(movingAverageStream).union(autoregressivStream).addSink(sinkFunction);

		// execute program
		JobExecutionResult result = env.execute("Streaming read CSV file");
		System.out.println("Execution time: " + result.getNetRuntime(TimeUnit.MILLISECONDS) + " ms");
	}

}
