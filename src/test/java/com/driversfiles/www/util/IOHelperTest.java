package com.driversfiles.www.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class IOHelperTest {

	@TempDir
	Path tempDir;

	static class TrackingCloseable implements Closeable {
		boolean closed = false;
		final boolean throwOnClose;
		TrackingCloseable(boolean throwOnClose) { this.throwOnClose = throwOnClose; }
		public void close() throws IOException {
			closed = true;
			if (throwOnClose) throw new IOException("boom");
		}
	}

	@Test
	void closeHandlesNullAndEmptyVarargs() {
		assertDoesNotThrow(() -> IOHelper.close());
		assertDoesNotThrow(() -> IOHelper.close((Closeable[]) null));
		assertDoesNotThrow(() -> IOHelper.close((Closeable) null));
	}

	@Test
	void closeClosesAllAndSwallowsExceptions() {
		TrackingCloseable a = new TrackingCloseable(false);
		TrackingCloseable b = new TrackingCloseable(true);
		TrackingCloseable c = new TrackingCloseable(false);
		assertDoesNotThrow(() -> IOHelper.close(a, b, c));
		assertTrue(a.closed);
		assertTrue(b.closed);
		assertTrue(c.closed);
	}

	@Test
	void readStringReadsUtf8Content() throws IOException {
		Path file = tempDir.resolve("sample.txt");
		Files.write(file, "héllo wörld".getBytes(StandardCharsets.UTF_8));
		try (FileInputStream in = new FileInputStream(file.toFile())) {
			assertEquals("héllo wörld", IOHelper.readString(in, "UTF-8"));
		}
	}

	@Test
	void readStringNullStreamReturnsNull() {
		assertDoesNotThrow(() -> assertNull(IOHelper.readString(null, "UTF-8")));
	}

	@Test
	void readStringReadsLargeStreamsFully() throws IOException {
		byte[] data = new byte[300 * 1024];
		for (int i = 0; i < data.length; i++) data[i] = (byte) ('a' + (i % 26));
		String expected = new String(data, StandardCharsets.US_ASCII);
		String actual;
		try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
			actual = IOHelper.readString(in, "US-ASCII");
		}
		assertEquals(expected.length(), actual.length());
	}
}
