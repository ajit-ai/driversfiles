package com.driversfiles.www.company;

import com.driversfiles.www.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles company page requests.
 *
 * @author Ajit Kumar
 */
@Controller
public class CompanyDashboardController extends BaseController {

	@RequestMapping("/secure/company/dashboard")
	public String dashboard() {
		return "company_dashboard.page";
	}
}
