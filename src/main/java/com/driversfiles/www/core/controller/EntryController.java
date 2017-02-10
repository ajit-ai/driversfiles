package com.driversfiles.www.core.controller;

import com.driversfiles.www.auth.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles initial entry after authentication.
 *
 * @author Erik R. Jensen
 */
@Controller
public class EntryController extends BaseController {

	@RequestMapping("/secure/entry")
	public String entry(HttpServletRequest req) {
		if (req.isUserInRole(AuthService.ROLE_ADMIN)) {
			return "redirect:/secure/admin/dashboard";
		} else if (req.isUserInRole(AuthService.ROLE_COMPANY)) {
			return "redirect:/secure/company/dashboard";
		} else { // Driver
			return "redirect:/secure/driver/dashboard";
		}
	}

}
