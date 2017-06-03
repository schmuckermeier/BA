package view;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import com.dataartisans.data.DataPoint;
import com.dataartisans.sinks.InfluxDBSink;

import controller.StockDataHandler;
import controller.StockPriceMapper;

public class StockDataView {

	public static void main(String[] args) throws Exception {

		// Checking input parameters
		final ParameterTool params = ParameterTool.fromArgs(args);

		// set up the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// boiler plate for this demo
		env.setRestartStrategy(RestartStrategies.fixedDelayRestart(1000, 1000));
		// env.setParallelism(1);
		env.disableOperatorChaining();

		// make parameters available in the web interface
		env.getConfig().setGlobalJobParameters(params);

		// fetch stock data from api
		StockDataHandler.loadDataToLocalCache("NDAQ");

		// read the text file from given input path
		DataStream<String> textData = env.readTextFile(StockDataHandler.OUT_PATH);

		// get stock data
		SingleOutputStreamOperator<DataPoint<Double>> streamOperator = textData.map(new StockPriceMapper());

		// Write this sensor stream out to InfluxDB
		SinkFunction<DataPoint<Double>> sinkFunction = new InfluxDBSink<>("stockPrice");

		streamOperator.addSink(sinkFunction);

		// execute program
		env.execute("Streaming stock price");
	}

}
