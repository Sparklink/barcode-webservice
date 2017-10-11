package org.barcodeapi.server.cache;

import org.barcodeapi.core.utils.CodeUtils;

public class CachedObject {

	private final long timeCreated;

	private final byte[] barcodeData;

	private final String checksum;

	private long lastAccess = 0;

	public CachedObject(byte[] data) {

		this.timeCreated = System.currentTimeMillis();

		this.barcodeData = data;

		this.checksum = CodeUtils.getMD5Sum(data);
	}

	public long getTimeCreated() {

		return timeCreated;
	}

	public byte[] getData() {

		lastAccess = System.currentTimeMillis();
		return barcodeData;
	}

	public long getDataSize() {

		return barcodeData.length;
	}

	public String getChecksum() {

		return checksum;
	}

	public long getLastAccess() {

		return lastAccess;
	}
}
