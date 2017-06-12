package controller;

import java.util.Random;

import org.apache.flink.api.common.functions.RichMapFunction;

import com.dataartisans.data.DataPoint;

@SuppressWarnings("serial")
public class RandomDataGenerator extends RichMapFunction<DataPoint<Long>, DataPoint<Double>> {

	@Override
	public DataPoint<Double> map(DataPoint<Long> dataPoint) throws Exception {
		// System.out.println(new Date(dataPoint.getTimeStampMs()).toString());
		double phase = dataPoint.getValue() * 2 * Math.PI;
		Random r = new Random();
		return dataPoint.withNewValue(r.nextGaussian() * 0.5 + Math.sin(phase));
		// return dataPoint.withNewValue(Math.random() - 0.5);
	}

}
