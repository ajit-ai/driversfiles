package com.driversfiles.www.core.data;

/**
 * Provides helper methods for data classes.
 */
public class DataHelper {

	/**
	 * Formats a phone number so it looks like (###)###-####. If the phone number is not
	 * exactly 10 characters, it is returned as it was passed in.
	 *
	 * @param phoneNumber the phone number to format.
	 * @return the formatted phone number
	 */
	public static String formatPhoneNumber(String phoneNumber) {
		if (phoneNumber != null && phoneNumber.length() == 10) {
			return new StringBuffer("(").append(phoneNumber.substring(0, 3))
					.append(")").append(phoneNumber.substring(3, 6))
					.append("-").append(phoneNumber.substring(6, 10))
					.toString();
		}
		return phoneNumber;
	}
}
