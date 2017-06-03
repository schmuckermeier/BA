package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.apache.flink.shaded.com.google.common.io.Files;
import org.junit.Test;

import junit.framework.Assert;

public class StockDataHandlerTest {

	@Test
	public void testFetchAPIdata() {
		List<String> apiData = Collections.emptyList();
		try {
			apiData = StockDataHandler.fetchAPIdata("NDAQ");
			for (String line : apiData) {
				String[] split = line.split("/");
				Assert.assertTrue(split.length == 2);
				Double.parseDouble(split[1]);
				System.out.println("Test Fetch: " + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWriteToFile() {
		try {
			StockDataHandler.writeToFile(StockDataHandler.fetchAPIdata("NDAQ"));
			File file = new File(StockDataHandler.OUT_PATH);
			Assert.assertNotNull(file);

			List<String> readLines = Files.readLines(file, Charset.defaultCharset());

			for (String line : readLines) {
				String[] split = line.split("/");
				Assert.assertTrue(split.length == 2);
				Double.parseDouble(split[1]);
				System.out.println("Test Read: " + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
