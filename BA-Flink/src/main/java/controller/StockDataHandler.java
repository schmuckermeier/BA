package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.sling.commons.json.JSONObject;

public class StockDataHandler {
	public final static String OUT_PATH = "/Users/SophieSchmuckermeier/BA/BA-Flink/src/main/resources/stockdata.txt";

	public static void loadDataToLocalCache(String stockCode) {
		List<String> apiData = Collections.emptyList();
		try {
			apiData = fetchAPIdata(stockCode);
		} catch (IOException e) {
			System.out.println("Error while fetching the data!!");
			e.printStackTrace();
		}

		writeToFile(apiData);
	}

	/**
	 * Fetches stock price data from REST API. Set stockCode for different
	 * sources (examples: NDAQ --> Nasdaq , AAPL --> Apple Inc , BMWYY --> Bmw ,
	 * DJI --> Dow Jones)
	 *
	 * @param Code
	 *            of stock
	 * @return String concatenation of "timestamp/price"
	 * @throws IOException
	 */
	protected static List<String> fetchAPIdata(String stockCode) throws IOException {

		final String price = "4. close";
		List<String> keyValuePairs = new ArrayList<>();

		try {
			JSONObject json = new JSONObject(
					IOUtils.toString(new URL("http://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="
							+ stockCode + "&interval=1min&outputsize=full&apikey=ARDH")));
			JSONObject stockDataList = json.getJSONObject("Time Series (1min)");

			Iterator<String> stockDataIterator = stockDataList.keys();
			while (stockDataIterator.hasNext()) {
				String key = stockDataIterator.next();
				JSONObject stockData = stockDataList.getJSONObject(key);
				String value = stockData.getString(price);
				keyValuePairs.add(key + "/" + value);
			}
			return keyValuePairs;

		} catch (Exception e) {
			throw new IOException("Error while loading data from API");
		}
	}

	protected static void writeToFile(List<String> keyValuePairs) {
		File file = new File(OUT_PATH);
		try (FileWriter fw = new FileWriter(file)) {
			keyValuePairs.stream().forEach(pair -> {
				try {
					fw.write(pair);
					fw.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
