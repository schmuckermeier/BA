package view;

import controller.StockDataHandler;

public class LoadDataThread implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				StockDataHandler.loadDataToLocalCache("NDAQ");
				System.out.println(">>>>>Fetched new data");

				Thread.sleep(30000);
			} catch (InterruptedException e) {
			}
		}
	}

}
