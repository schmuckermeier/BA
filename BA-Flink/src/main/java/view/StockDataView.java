package view;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import com.dataartisans.data.DataPoint;
import com.dataartisans.sinks.InfluxDBSink;

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
		new Thread(new LoadDataThread()).start();

		// Create stream out to InfluxDB
		SinkFunction<DataPoint<Double>> sinkFunction = new InfluxDBSink<>("stockPrice");

		// write Data to Grafana
		new Thread(new WriteDataToSinkThread(env, sinkFunction)).start();
	}

}
