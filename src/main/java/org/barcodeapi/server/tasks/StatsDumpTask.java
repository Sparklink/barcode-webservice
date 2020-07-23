package org.barcodeapi.server.tasks;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.barcodeapi.server.core.BackgroundTask;
import org.barcodeapi.server.core.Log;
import org.barcodeapi.server.core.Log.LOG;

public class StatsDumpTask extends BackgroundTask {

	private static final String _TELEMETRY_URL = "https://barcodeapi.org/stats/upload";

	private final boolean telemetry;

	public StatsDumpTask(boolean telemetry) {
		super();

		this.telemetry = telemetry;
	}

	@Override
	public void onRun() {

		// get metric data
		String data = getStats().dumpJSON().toString();

		// print to the log
		Log.out(LOG.SERVER, "STATS : " + data);

		// skip if not enabled
		if (!telemetry) {
			return;
		}

		try {

			// create http client
			URL url = new URL(_TELEMETRY_URL);
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) con;

			// set request options
			http.setDoOutput(true);
			http.setRequestMethod("POST");
			http.setFixedLengthStreamingMode(data.length());
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			// connect and write
			http.connect();
			http.getOutputStream().write(data.getBytes());
			http.disconnect();

		} catch (Exception e) {

			Log.out(LOG.SERVER, "Failed to upload telemetry data: " + e.getLocalizedMessage());
		}
	}
}
