package org.barcodeapi.server.api;

import org.barcodeapi.core.ServerRuntime;
import org.barcodeapi.server.core.RestHandler;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AboutHandler extends RestHandler {

	public AboutHandler() {
		super();
	}

	@Override
	protected void onRequest(HttpServletRequest request, HttpServletResponse response)
			throws JSONException, IOException {

		// print response to client
		JSONObject output = new JSONObject()//
				.put("runtimeId", ServerRuntime.getRuntimeID())//
				.put("uptime", ServerRuntime.getTimeRunning())//
				.put("admin", "---")//
				.put("hostname", ServerRuntime.getHostname())//
				.put("version", ServerRuntime.getVersion());
		response.getOutputStream().println(output.toString(4));
		response.setHeader("Content-Type", "application/json");
	}
}
