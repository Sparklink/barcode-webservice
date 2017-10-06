package org.barcodeapi.test.gen;

import org.barcodeapi.server.ServerTestBase;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class TestAutoType extends ServerTestBase {

	@Test
	public void testAutoType_Ean8() {

		apiGet("/00000000");

		Assert.assertEquals("Response Code", //
				HttpStatus.OK_200, getResponseCode());

		Assert.assertEquals("Code Type", //
				"EAN8", getHeader("X-CodeType"));

		Assert.assertEquals("Code Data", //
				"00000000", getHeader("X-CodeData"));
	}

	@Test
	public void testAutoType_Ean8With7() {

		apiGet("/0000000");

		Assert.assertEquals("Response Code", //
				HttpStatus.OK_200, getResponseCode());

		Assert.assertEquals("Code Type", //
				"EAN8", getHeader("X-CodeType"));

		Assert.assertEquals("Code Data", //
				"0000000", getHeader("X-CodeData"));
	}

	@Test
	public void testAutoType_Ean13() {

		apiGet("/0000000000000");

		Assert.assertEquals("Response Code", //
				HttpStatus.OK_200, getResponseCode());

		Assert.assertEquals("Code Type", //
				"EAN13", getHeader("X-CodeType"));

		Assert.assertEquals("Code Data", //
				"0000000000000", getHeader("X-CodeData"));
	}

	@Test
	public void testAutoType_Ean13With12() {

		apiGet("/000000000000");

		Assert.assertEquals("Response Code", //
				HttpStatus.OK_200, getResponseCode());

		Assert.assertEquals("Code Type", //
				"EAN13", getHeader("X-CodeType"));

		Assert.assertEquals("Code Data", //
				"000000000000", getHeader("X-CodeData"));
	}

	@Test
	public void testAutoType_Code39() {

		apiGet("/ABC123");

		Assert.assertEquals("Response Code", //
				HttpStatus.OK_200, getResponseCode());

		Assert.assertEquals("Code Type", //
				"Code39", getHeader("X-CodeType"));

		Assert.assertEquals("Code Data", //
				"ABC123", getHeader("X-CodeData"));
	}

	@Test
	public void testAutoType_Code128() {

		apiGet("/abc123");

		Assert.assertEquals("Response Code", //
				HttpStatus.OK_200, getResponseCode());

		Assert.assertEquals("Code Type", //
				"Code128", getHeader("X-CodeType"));

		Assert.assertEquals("Code Data", //
				"abc123", getHeader("X-CodeData"));
	}

	@Test
	public void testAutoType_QRCode() {

		apiGet("/$");

		Assert.assertEquals("Response Code", //
				HttpStatus.OK_200, getResponseCode());

		Assert.assertEquals("Code Type", //
				"QRCode", getHeader("X-CodeType"));

		Assert.assertEquals("Code Data", //
				"$", getHeader("X-CodeData"));
	}

	@Test
	public void testAutoType_DataMatrix() {

		// Build 64 char request
		String req = "";
		for (int x = 0; x <= 64; x++) {
			req += "0";
		}

		apiGet("/" + req);

		Assert.assertEquals("Response Code", //
				HttpStatus.OK_200, getResponseCode());

		Assert.assertEquals("Code Type", //
				"DataMatrix", getHeader("X-CodeType"));

		Assert.assertEquals("Code Data", //
				req, getHeader("X-CodeData"));
	}
}
