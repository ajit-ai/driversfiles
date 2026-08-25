package com.driversfiles.www.core.controller;

import com.driversfiles.www.captcha.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * Handles public page requests.
 *
 * @author Erik R. Jensen
 */
@Controller
public class PublicController extends BaseController {

	@Autowired
	@Qualifier("captchaService")
	private CaptchaService captchaService;

	@RequestMapping(value = {"/home", "/"})
	public String home() {
		return "home.page";
	}

	@RequestMapping(value = "/features")
	public String features() {
		return "features.page";
	}

	@RequestMapping(value = "/faq")
	public String faq() {
		return "faq.page";
	}

	@RequestMapping(value = "/contactus")
	public String contactUs() {
		return "contactus.page";
	}

	@RequestMapping("/login")
	public String login() {
		return "login.page";
	}

	@RequestMapping(value = "/captcha/image.htm", method = RequestMethod.GET)
	public void captcha(HttpServletResponse response, HttpSession session, Locale locale) throws IOException {
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		captchaService.writeImage(response.getOutputStream(), session.getId(), locale);
	}
}
