package com.driversfiles.www.company;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.State;

/**
 * Handles My Drivers requests.
 *
 * @author Mark Burns
 */
@Controller
public class MyDriversController {

	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	@InitBinder
	public void binder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@ModelAttribute("states")
	State[] getStates() {
		return State.values();
	}

	@RequestMapping(value = "/secure/company/drivers", method = RequestMethod.GET)
	public String drivers(@ModelAttribute("driverSearchForm") DriverSearchForm form, HttpServletRequest req) {
		
		List<Driver> drivers = driverDao.getDriversFilteredByEffectiveUser(null, null, null);
		req.setAttribute("drivers", drivers);
		
		return "company_drivers.page";
	}
	
	@RequestMapping(value = "/secure/company/drivers", method = RequestMethod.POST)
	public String driversSearch(@ModelAttribute("driverSearchForm") DriverSearchForm form, HttpServletRequest req) {
		if (req.getParameter("clear") != null) {
			form.clear();
		}
		
		List<Driver> drivers = driverDao.getDriversFilteredByEffectiveUser(form.getFirstName(), form.getLastName(), form.getEmail());
		req.setAttribute("drivers", drivers);
		return "company_drivers.page";
	}
	
	public static class DriverSearchForm {
		
		private String firstName;
		private String lastName;
		private String email;
		
		public void clear() {
			firstName = null;
			lastName = null;
			email = null;
		}
		
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}

}
