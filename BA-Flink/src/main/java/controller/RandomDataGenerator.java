package controller;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.flink.api.common.functions.RichMapFunction;

import com.dataartisans.data.DataPoint;

import model.StreamValue;

@SuppressWarnings("serial")
public class RandomDataGenerator extends RichMapFunction<DataPoint<Long>, DataPoint<Double>> {

	public Stream<StreamValue> createStreamData() {
		IntStream values = new Random().ints(0, 100);
		Stream<StreamValue> stream = values.mapToObj(StreamValue::new);
		return stream;
	}

	@Override
	public DataPoint<Double> map(DataPoint<Long> dataPoint) throws Exception {
		return dataPoint.withNewValue(Math.random());
	}

}
