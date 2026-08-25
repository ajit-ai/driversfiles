package com.driversfiles.www.driver;

import jakarta.servlet.http.HttpServletRequest;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.controller.BaseController;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import com.driversfiles.www.core.service.AccessCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles driver page requests.
 *
 * @author Ajit Kumar
 */
@Controller
public class DriverDashboardController extends BaseController {

	@Autowired
	@Qualifier("accessCodeService")
	private AccessCodeService accessCodeService;

	@Autowired
	@Qualifier("authService")
	private AuthService authService;

	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	@RequestMapping("/secure/driver/dashboard")
	public String dashboard() {
		return "driver_dashboard.page";
	}

	@RequestMapping("/secure/driver/welcome")
	public String welcome() {
		return "driver_welcome.page";
	}

	@RequestMapping("/secure/driver/accesscode")
	public String accessCode(HttpServletRequest req) {
		
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
			Driver driver = person.getDriver();
			if (driver.getAccessCode() == null) {
				accessCodeService.generateNewAccessCode(driver);
				driver = driverDao.find(driver.getId());
			}
			req.setAttribute("driver", driver);
			req.setAttribute("expireDate", driver.getAccessCodeExpireDate());
		}
		req.setAttribute("appUrl", getApplicationFormUrl(req));
		
		
		return "driver_accesscode.page";
	}
	
	@RequestMapping("/secure/driver/accesscode/print")
	public String accessCodePrint(HttpServletRequest req) {
		
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
			Driver driver = person.getDriver();
			req.setAttribute("driver", driver);
			req.setAttribute("expireDate", driver.getAccessCodeExpireDate());
		}
		req.setAttribute("appUrl", getApplicationFormUrl(req));
		
		return "driver_accesscode_print.page";
	}
	
	@RequestMapping("/secure/driver/accesscode/{accessCode}/expire")
	public String expireAccessCode(@PathVariable String accessCode, HttpServletRequest req) {
		
		Driver driver = driverDao.getDriverByAccessCode(accessCode);
		if (driver != null)
			accessCodeService.generateNewAccessCode(driver);
		
		return "redirect:/secure/driver/accesscode";
	}
	
	private String getApplicationFormUrl(HttpServletRequest req) {
		
		StringBuffer url = req.getRequestURL();
		String root = url.substring(0, url.indexOf("secure"));
		return root + "application";
	}
	
}
