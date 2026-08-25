package com.driversfiles.www.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public final class IOHelper {

	private IOHelper() {}

	public static void close(Closeable... closeables) {
		if (closeables != null) {
			for (Closeable c : closeables) {
				if (c != null) {
					try {
						c.close();
					} catch (IOException x) {
						// ignore
					}
				}
			}
		}
	}

	public static String readString(InputStream in, String charset) throws IOException {
		if (in == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[100 * 1024];
		int read = in.read(buf);
		while (read != -1) {
			out.write(buf, 0, read);
			read = in.read(buf);
		}
		return out.toString(charset);
	}
}
