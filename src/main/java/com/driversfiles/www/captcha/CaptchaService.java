package com.driversfiles.www.captcha;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * This service handles call captcha related functionality.
 * 
 * @author Erik R. Jensen
 */
public interface CaptchaService {

	/**
	 * Writes a PNG captcha image out to the given output stream.
	 * This method will flush and close the given OutputStream.
	 *
	 * @param out the stream to write to
	 * @param captchaId the id to associate with the captcha
	 * @param locale the local in which to generate the captcha
	 * @throws IOException if an error occurs
	 */
	public void writeImage(OutputStream out, String captchaId, Locale locale) throws IOException;

	/**
	 * Validates a captcha response from an image captcha.
	 *
	 * @param captchaId the id associated with the captcha
	 * @param response the response from the user
	 * @return true if the response passes validation, false if otherwise
	 */
	public boolean validateImageResponse(String captchaId, String response);
}
