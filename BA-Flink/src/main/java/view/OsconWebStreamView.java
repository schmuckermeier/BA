package view;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.dataartisans.data.DataPoint;
import com.dataartisans.sinks.InfluxDBSink;
import com.dataartisans.sources.TimestampSource;

import controller.RandomDataGenerator;

public class OsconWebStreamView {

	public static void main(String[] args) throws Exception {

		// set up the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// Simulate some sensor data
		DataStream<DataPoint<Double>> sensorStream = generateSensorData(env);

		// Write this sensor stream out to InfluxDB
		sensorStream.addSink(new InfluxDBSink<>("random"));

		// execute program
		env.execute("Random Data Generator");
	}

	private static DataStream<DataPoint<Double>> generateSensorData(StreamExecutionEnvironment env) {

		// boiler plate for this demo
		env.setRestartStrategy(RestartStrategies.fixedDelayRestart(1000, 1000));
		env.setParallelism(1);
		env.disableOperatorChaining();

		final int PERIOD_MS = 50;
		final int SLOWDOWN_FACTOR = 1;

		// create timestamp
		DataStreamSource<DataPoint<Long>> timestampSource = env
				.addSource(new TimestampSource(PERIOD_MS, SLOWDOWN_FACTOR), "timestamp");

		// add random data
		SingleOutputStreamOperator<DataPoint<Double>> dataStream = timestampSource.map(new RandomDataGenerator())
				.name("random data");

		return dataStream;
	}

}
