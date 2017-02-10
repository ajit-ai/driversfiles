package com.driversfiles.www.core.controller;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.PersonType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Handles sign up.
 * @author Erik R. Jensen
 */
@Controller
public class SignUpController extends BaseController {

	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	@Autowired
	@Qualifier("personDao")
	private PersonDao personDao;

	@Autowired
	@Qualifier("authService")
	private AuthService authService;

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signUp(@ModelAttribute("signUpForm") SignUpForm form) {
		return "signup.page";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signUpPost(@ModelAttribute("signUpForm") @Valid SignUpForm form, Errors errors) {
		// Validate email
		// Validate passwords

		if (errors.hasErrors()) { // Check declarative validation
			return "signup.page";
		}
		Person p = personDao.findByEmail(form.getEmail1());
		if (p != null) {
			errors.rejectValue("email1", "error.emailInUse");
		}
		if (!form.getEmail1().equals(form.getEmail2())) {
			errors.rejectValue("email2", "error.emailNoMatch");
		}
		if (!form.getPassword1().equals(form.getPassword2())) {
			errors.rejectValue("password2", "error.passwordNoMatch");
		}
		if (errors.hasErrors()) { // Check programmatic validation
			return "signup.page";
		}

		p = new Person();
		p.setFirstName(form.getFirstName());
		p.setMiddleName(form.getMiddleName());
		p.setLastName(form.getLastName());
		p.setEmail(form.getEmail1());
		p.setPassword(form.getPassword1());
		p.setType(PersonType.DRIVER);
		personDao.save(p);
		Driver d = new Driver();
		d.setPerson(p);
		driverDao.save(d);
		authService.authenticate(p.getEmail());
		return "redirect:/secure/driver/welcome";
	}

	public static class SignUpForm {

		@NotEmpty
		private String firstName;

		private String middleName;

		@NotEmpty
		private String lastName;

		@Email
		@NotEmpty
		private String email1;

		@Email
		@NotEmpty
		private String email2;

		@NotEmpty
		private String password1;

		@NotEmpty
		private String password2;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getMiddleName() {
			return middleName;
		}

		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail1() {
			return email1;
		}

		public void setEmail1(String email1) {
			this.email1 = email1;
		}

		public String getEmail2() {
			return email2;
		}

		public void setEmail2(String email2) {
			this.email2 = email2;
		}

		public String getPassword1() {
			return password1;
		}

		public void setPassword1(String password1) {
			this.password1 = password1;
		}

		public String getPassword2() {
			return password2;
		}

		public void setPassword2(String password2) {
			this.password2 = password2;
		}
	}
}
