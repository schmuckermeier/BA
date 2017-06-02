package controller;

import java.util.stream.Stream;

import org.junit.Test;

import model.StreamValue;

public class FakeDataGeneratorTest {

	@Test
	public void testCreateStreaData() {
		Stream<StreamValue> stream = new FakeDataGenerator().createStreamData();
		stream.forEach(v -> System.out.println(v.toSting()));
	}

}
