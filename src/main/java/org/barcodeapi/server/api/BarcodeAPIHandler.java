package org.barcodeapi.server.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.barcodeapi.server.cache.CachedObject;
import org.barcodeapi.server.core.GenerationException;
import org.barcodeapi.server.core.RestHandler;
import org.barcodeapi.server.gen.BarcodeGenerator;
import org.eclipse.jetty.server.Request;

public class BarcodeAPIHandler extends RestHandler {

	private final CachedObject ERR;
	private final CachedObject BLK;

	public BarcodeAPIHandler() {

		try {

			ERR = BarcodeGenerator.requestBarcode("/128/$$@E$$@R$$@R$$@O$$@R$$@");
			BLK = BarcodeGenerator.requestBarcode("/128/$$@B$$@L$$@A$$@C$$@K$$@L$$@I$$@S$$@T$$@");
		} catch (GenerationException e) {
			throw new RuntimeException("init failed");
		}
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		super.handle(target, baseRequest, request, response);

		// the current time
		long timeStart = System.currentTimeMillis();

		// get source of the request
		String source;
		String ref = request.getHeader("Referer");
		if (ref != null) {
			source = ref;
		} else {
			source = "API";
		}

		// get users IP
		String from;
		String via = request.getRemoteAddr();
		String ip = request.getHeader("X-Forwarded-For");
		if (ip != null) {
			from = ip + " ] via [ " + via;
		} else {
			from = via;
		}

		CachedObject barcode;
		try {

			// generate user requested barcode
			barcode = BarcodeGenerator.requestBarcode(target);

		} catch (GenerationException e) {

			String message = "Failed [ " + target + " ]" + //
					" reason [ " + e.toString() + " ]";
			System.out.println(System.currentTimeMillis() + " : " + message);

			switch (e.getExceptionType()) {
			case BLACKLIST:
				// serve blacklist code
				barcode = BLK;
				break;

			case EMPTY:
			case FAILED:
			default:
				// serve error code
				barcode = ERR;
				break;
			}

			// set HTTP response code and add message to headers
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setHeader("X-Error-Message", message);
		}

		// time it took to process request
		long time = System.currentTimeMillis() - timeStart;

		// additional properties
		String type = barcode.getProperties().getProperty("type");
		String data = barcode.getProperties().getProperty("data");
		String nice = barcode.getProperties().getProperty("nice");
		String encd = barcode.getProperties().getProperty("encd");

		System.out.println(System.currentTimeMillis() + " : " + //
				"Served [ " + type + " ] " + //
				"with [ " + data + " ] " + //
				"in [ " + time + "ms ] " + //
				"size [ " + barcode.getDataSize() + "B ] " + //
				"using [ " + source + " ] " + //
				"for [ " + from + " ]");

		// FIXME session
		// session.onRender(data);

		// add cache headers
		response.setHeader("Cache-Control", "max-age=86400, public");

		// add content headers
		response.setHeader("Content-Type", "image/png");
		response.setHeader("Content-Length", Long.toString(barcode.getDataSize()));

		// file name when clicking save
		response.setHeader("Content-Disposition", "filename=" + nice + ".png");

		// barcode details
		response.setHeader("X-Barcode-Type", type);
		response.setHeader("X-Barcode-Content", encd);

		// print image to stream
		response.getOutputStream().write(barcode.getData());
	}
}
