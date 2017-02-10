package com.driversfiles.www.fs;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic utility for checking PDF files
 *
 * @author Mark Burns
 */
public class PdfUtil {
	
	private InputStream in;
	private DataInput din;

	
	/**
	 * Set the input stream to the argument stream (or file).
	 * 
	 * @param inputStream the input stream to read from
	 */
	public void setInput(InputStream inputStream) {
		in = inputStream;
		din = null;
	}

	/**
	 * This is a very basic check whether the inputStream represents a PDF file
	 * 
	 * @return boolean is a PDF file
	 */
	public boolean check() {

		try {
			int b1 = read() & 0xff;
			int b2 = read() & 0xff;
			int b3 = read() & 0xff;
			int b4 = read() & 0xff;
			int b5 = read() & 0xff;
			if (b1 == 0x25 && // %
				b2 == 0x50 && // P
				b3 == 0x44 && // D
				b4 == 0x46 && // F
				b5 == 0x2D // -
				) {
				return true;
			} else {
				return false;
			}
		} catch (IOException ioe) {
			return false;
		}
	}
	
	private int read() throws IOException {
		if (in != null) {
			return in.read();
		} else {
			return din.readByte();
		}
	}

}
