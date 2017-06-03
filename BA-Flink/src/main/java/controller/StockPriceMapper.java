package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.flink.api.common.functions.MapFunction;

import com.dataartisans.data.DataPoint;

@SuppressWarnings("serial")
public class StockPriceMapper implements MapFunction<String, DataPoint<Double>> {

	@Override
	public DataPoint<Double> map(String line) throws Exception {
		String[] split = line.split("/");
		long timestamp = timestampConvertion(split[0]);
		double value = Double.parseDouble(split[1]);
		System.out.println(new Date(timestamp).toString() + " -- Timestamp: " + timestamp + "  Value: " + value);
		return new DataPoint<>(timestamp, value);

	}

	protected static long timestampConvertion(String timestampString) throws ParseException {
		Date date = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = format.parse(timestampString);
			return date.getTime();
		} catch (ParseException e) {
			throw new ParseException("timeStamp in wrong format", e.getErrorOffset());
		}
	}

}
