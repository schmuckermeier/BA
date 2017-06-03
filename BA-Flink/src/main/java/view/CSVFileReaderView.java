package view;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import com.dataartisans.data.DataPoint;
import com.dataartisans.sinks.InfluxDBSink;

import controller.CSVMapFunktion;

public class CSVFileReaderView {
	public static void main(String[] args) throws Exception {

		// Checking input parameters
		final ParameterTool params = ParameterTool.fromArgs(args);

		// set up the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// TODO: ask why graph changes with different parallelism!!!!
		// boiler plate for this demo
		env.setRestartStrategy(RestartStrategies.fixedDelayRestart(1000, 1000));
		// env.setParallelism(1);
		env.disableOperatorChaining();

		// make parameters available in the web interface
		env.getConfig().setGlobalJobParameters(params);

		// read the text file from given input path
		DataStream<String> textData = env.readTextFile(params.get("input"));

		SingleOutputStreamOperator<DataPoint<Double>> streamOperator = textData.map(new CSVMapFunktion(3, 2));

		// Write this sensor stream out to InfluxDB
		SinkFunction<DataPoint<Double>> sinkFunction = new InfluxDBSink<>("csvFile");

		streamOperator.addSink(sinkFunction);

		// execute program
		env.execute("Streaming read CSV file");
	}

}
