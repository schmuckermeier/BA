package view;

import java.util.Random;

import org.apache.flink.api.common.functions.ReduceFunction;

import com.dataartisans.data.KeyedDataPoint;

public class AutoregessiveReduceFunction implements ReduceFunction<KeyedDataPoint<Double>> {

	private static final long serialVersionUID = 8008111294350144593L;
	private final Random r = new Random();

	@Override
	public KeyedDataPoint<Double> reduce(KeyedDataPoint<Double> x1, KeyedDataPoint<Double> x2) throws Exception {
		// starting point
		final double C = 6.5;
		// weight of period before
		final double D = -0.5;

		double prediction = C + D * x1.getValue() + r.nextGaussian() * 1.8;

		return new KeyedDataPoint<>("autogegressive", x2.getTimeStampMs(), prediction);
	}

}
