package model;

import java.util.Date;

import com.dataartisans.data.DataPoint;

/**
 * Can be replaced by DataPoint
 *
 * @author SophieSchmuckermeier
 *
 */
@Deprecated
public class StreamValue {

	int value;
	Date timestamp;

	public StreamValue(int value) {
		this.value = value;
		this.timestamp = new Date();
	}

	public StreamValue(long timestamp, int value) {
		this.value = value;
		this.timestamp = new Date(timestamp);
	}

	public static StreamValue readLine(String line) {
		String[] split = line.split(",");
		long timestamp = Long.parseLong(split[2]);
		int value = Integer.parseInt(split[1]);
		return new StreamValue(timestamp, value);
	}

	public static DataPoint<Integer> readDataPoint(String line) {
		String[] split = line.split(",");
		long timestamp = Long.parseLong(split[2]);
		int value = Integer.parseInt(split[1]);
		return new DataPoint<>(timestamp, value);
	}

	public static DataPoint<Integer> createDataPoint(StreamValue v) {
		return new DataPoint<>(v.getTimestamp().getTime(), v.getValue());
	}

	public String toSting() {
		String time = timestamp.toString();
		return time + " : " + value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public int getValue() {
		return value;
	}
}