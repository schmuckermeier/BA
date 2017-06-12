package view;

import java.util.Random;

import org.apache.flink.api.common.functions.MapFunction;

import com.dataartisans.data.KeyedDataPoint;

@SuppressWarnings("serial")
public class MovingAverageMapFunction implements MapFunction<KeyedDataPoint<Double>, KeyedDataPoint<Double>> {

	double lastError = 0;
	private final Random r = new Random();

	@Override
	public KeyedDataPoint<Double> map(KeyedDataPoint<Double> dataPoint) throws Exception {
		// starting point
		final double C = 4.3;

		// the weight of the last error; needs to be between -1 < D < 1
		final double D = 0.8;

		double newError = r.nextGaussian() * 1.5;

		double prediction = C + newError + D * lastError;
		lastError = newError;
		dataPoint.withNewKeyAndValue("moving average", prediction);
		return dataPoint.withNewKeyAndValue("moving average", prediction);
	}

}
