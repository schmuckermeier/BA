package controller;

import java.util.Date;

import org.apache.flink.api.common.functions.MapFunction;

import com.dataartisans.data.KeyedDataPoint;

@SuppressWarnings("serial")
public class CSVMapFunktion implements MapFunction<String, KeyedDataPoint<Double>> {

	final int TIMESTAMP_POSITION;
	final int VALUE_POSITION;

	/**
	 * Position of time stamp and the actual value in the CSV file. Starting
	 * with zero.
	 *
	 * @param timestampPosition
	 *            as long
	 * @param valuePosition
	 *            as double (e.g. 3.42)
	 */
	public CSVMapFunktion(int timestampPosition, int valuePosition) {
		TIMESTAMP_POSITION = timestampPosition;
		VALUE_POSITION = valuePosition;
	}

	@Override
	public KeyedDataPoint<Double> map(String line) {
		String[] split = line.split(",");
		try {
			double value = Double.parseDouble(split[VALUE_POSITION]);
			long timestamp = Long.parseLong(split[TIMESTAMP_POSITION]);
			System.out.println(new Date(timestamp).toString() + " -- Timestamp: " + timestamp + "  Value: " + value);
			return new KeyedDataPoint<>("csv data", timestamp, value);
		} catch (Exception e) {
			throw new NumberFormatException("Format of line doesn't fit or line is empty.");
		}
	}

}
