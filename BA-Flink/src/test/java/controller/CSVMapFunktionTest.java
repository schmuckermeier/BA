package controller;

import org.junit.Test;

import com.dataartisans.data.DataPoint;

import junit.framework.Assert;

public class CSVMapFunktionTest {

	@Test
	public void testMap() {
		String line = "h2o_feet,coyote_creek,6.923,1440932400";
		DataPoint<Double> dataPoint = new CSVMapFunktion(3, 2).map(line);
		Assert.assertEquals(1440932400, dataPoint.getTimeStampMs());
		Assert.assertEquals(6.923, dataPoint.getValue());
	}

}
