package com.driversfiles.www.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Person;

/**
 * Handles My Account requests.
 *
 * @author Ajit Kumar
 */
@Controller
public class AccountController {

	@Autowired
	@Qualifier("authService")
	private AuthService authService;

	@Autowired
	@Qualifier("personDao")
	private PersonDao personDao;

	@RequestMapping(value = "/secure/common/accounts/{personUuid}", method = RequestMethod.GET)
	public String account(@PathVariable String personUuid, @ModelAttribute("myAccountForm") MyAccountForm form, HttpServletRequest req) {
		
		Person person = personDao.findByUuid(personUuid);
		form.setUuid(personUuid);
		form.setFirstName(person.getFirstName());
		form.setLastName(person.getLastName());
		form.setEmail(person.getEmail());
		
		return person.getType().name().toLowerCase() + "_account.page";
	}

	@RequestMapping(value = "/secure/common/account/save", method = RequestMethod.POST)
	public String saveAccount(@ModelAttribute("myAccountForm") @Valid MyAccountForm form, Errors errors, HttpServletRequest req) {
		
		Person person = personDao.findByUuid(form.getUuid());
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return person.getType().name().toLowerCase() + "_account.page";
		}
		
		person.setFirstName(form.getFirstName());
		person.setLastName(form.getLastName());
		person.setEmail(form.getEmail());
		personDao.update(person);
		
		// Change password if necessary
		if (form.getNewPassword() != null && !form.getNewPassword().isEmpty()) {
			if (form.getNewPassword().equals(form.getConfirmPassword())) {
				// Attempt to change password
				if (!authService.isUserInRole("ROLE_PREVIOUS_ADMINISTRATOR")) {
					// Check the old password
					if (!authService.isPasswordValid(form.getCurrentPassword(), person.getEmail()))
					{
						errors.rejectValue("currentPassword", "error.passwordNoMatch");
						return person.getType().name().toLowerCase() + "_account.page";
					}
				}
				
				person.setPassword(form.getNewPassword());
				personDao.update(person);
			} else {
				errors.rejectValue("newPassword", "error.passwordNoMatch");
				return person.getType().name().toLowerCase() + "_account.page";
			}
		}
		
		return "redirect:/secure/common/accounts/" + form.getUuid() + "?message=success";
	}
	
	public static class MyAccountForm {
		
		@NotEmpty
		private String uuid;
		@NotEmpty
		private String firstName;
		@NotEmpty
		private String lastName;
		@NotEmpty
		@Email
		private String email;
		private String currentPassword;
		private String newPassword;
		private String confirmPassword;
		
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
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
		public String getCurrentPassword() {
			return currentPassword;
		}
		public void setCurrentPassword(String currentPassword) {
			this.currentPassword = currentPassword;
		}
		public String getNewPassword() {
			return newPassword;
		}
		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}
		public String getConfirmPassword() {
			return confirmPassword;
		}
		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
	}
}
