package com.driversfiles.www.admin;

import com.driversfiles.www.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles common admin pages.
 *
 * @author Ajit Kumar
 */
@Controller
public class AdminDashboardController extends BaseController {

	@RequestMapping("/secure/admin/dashboard")
	public String dashboard() {
		return "admin_dashboard.page";
	}
}
