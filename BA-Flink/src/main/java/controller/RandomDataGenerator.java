package controller;

import org.apache.flink.api.common.functions.RichMapFunction;

import com.dataartisans.data.DataPoint;

@SuppressWarnings("serial")
public class RandomDataGenerator extends RichMapFunction<DataPoint<Long>, DataPoint<Double>> {

	@Override
	public DataPoint<Double> map(DataPoint<Long> dataPoint) throws Exception {
		return dataPoint.withNewValue(Math.random());
	}

}
