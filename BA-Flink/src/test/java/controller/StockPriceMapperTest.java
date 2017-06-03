package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.dataartisans.data.DataPoint;

import controller.StockPriceMapper;

public class StockPriceMapperTest {

	@Test
	public void testMap() {
		String line = "2017-05-19 09:34:00/66.6400";
		String[] split = line.split("/");
		String expectedTimestampString = split[0];
		Double expectedPrice = Double.parseDouble(split[1]);

		try {
			DataPoint<Double> dataPoint = new StockPriceMapper().map(line);

			long timeStampMs = dataPoint.getTimeStampMs();
			Date date = new Date(timeStampMs);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeStampString = format.format(date);

			Assert.assertEquals(expectedTimestampString, timeStampString);

			Double price = dataPoint.getValue();
			Assert.assertEquals(expectedPrice, price);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTimestampConvertion() throws ParseException {
		String timestampString = "2017-06-02 15:59:00";
		long timestamp = StockPriceMapper.timestampConvertion(timestampString);

		LocalDateTime expectedDate = LocalDateTime.of(2017, 6, 2, 15, 59, 0);
		long expectedLong = expectedDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Assert.assertEquals(expectedLong, timestamp);
	}

}
