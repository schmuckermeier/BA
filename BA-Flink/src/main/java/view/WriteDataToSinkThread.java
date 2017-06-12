package view;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import com.dataartisans.data.DataPoint;

import controller.StockDataHandler;
import controller.StockPriceMapper;

public class WriteDataToSinkThread implements Runnable {

	private final StreamExecutionEnvironment env;
	private final SinkFunction<DataPoint<Double>> sinkFunction;

	public WriteDataToSinkThread(StreamExecutionEnvironment env, SinkFunction<DataPoint<Double>> sinkFunction) {
		this.env = env;
		this.sinkFunction = sinkFunction;
	}

	@Override
	public void run() {
		while (true) {
			// read the text file from given input path
			DataStream<String> textData = env.readTextFile(StockDataHandler.OUT_PATH);

			// get stock data
			SingleOutputStreamOperator<DataPoint<Double>> streamOperator = textData.map(new StockPriceMapper());

			streamOperator.addSink(sinkFunction);

			try {
				env.execute("Streaming stock price");
				System.out.println(">>>>>Sended new data");

				Thread.sleep(10000);
			} catch (Exception e1) {
			}

		}

	}
}
