package controller;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import model.StreamValue;

public class FakeDataGenerator {

	public Stream<StreamValue> createStreamData() {
		IntStream values = new Random().ints(0, 100);
		Stream<StreamValue> stream = values.mapToObj(StreamValue::new);
		return stream;
	}

}
