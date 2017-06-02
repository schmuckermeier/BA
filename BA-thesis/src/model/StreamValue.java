package model;

import java.util.Date;

public class StreamValue {

	int value;
	Date timestamp;

	public StreamValue(int value) {
		this.value = value;
		this.timestamp = new Date();
	}

	public String toSting() {
		String time = timestamp.toString();
		return time + " : " + value;
	}
}