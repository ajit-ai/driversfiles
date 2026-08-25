package com.driversfiles.www.admin;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.driversfiles.www.core.controller.BaseController;
import com.driversfiles.www.core.dao.CompanyDao;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;

/**
 * Handles all user management requests.
 *
 * @author Erik R. Jensen
 */
@Controller
@SessionAttributes("userManagementForm")
public class UserManagementController extends BaseController {

	@Autowired
	@Qualifier("personDao")
	private PersonDao personDao;

	@Autowired
	@Qualifier("companyDao")
	private CompanyDao companyDao;

	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	public static class PersonForm {

		@NotEmpty
		private String firstName;

		@NotEmpty
		private String lastName;

		@NotEmpty
		@Email
		private String email;

		private String password1;
		private String password2;

		@NotNull
		private PersonType type;

		private String companyName;
		private String companyNumber;

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

		public PersonType getType() {
			return type;
		}

		public void setType(PersonType type) {
			this.type = type;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getCompanyNumber() {
			return companyNumber;
		}

		public void setCompanyNumber(String companyNumber) {
			this.companyNumber = companyNumber;
		}
	}

	@ModelAttribute("personTypes")
	PersonType[] getPersonTypes() {
		return PersonType.values();
	}

	@RequestMapping(value = "/secure/admin/users", method = RequestMethod.GET)
	public String users(HttpServletRequest req, ModelMap model) {
		UserManagementForm form = (UserManagementForm)model.get("userManagementForm");
		if (form == null) {
			form = new UserManagementForm();
			model.addAttribute("userManagementForm", form);
		}
		List<Person> people = personDao.getPeople(form.getFirstName(), form.getLastName(), form.getEmail(),
				form.getType(), form.getCompanyName(), form.getCompanyNumber(), 0, form.getMaxSize());
		req.setAttribute("people", people);
		return "admin_users.page";
	}
	
	@RequestMapping(value = "/secure/admin/users", method = RequestMethod.POST)
	public String usersPost(@ModelAttribute("userManagementForm") @Valid UserManagementForm form, Errors errors) {
		if (errors.hasErrors()) {
			return "admin_users.page";
		}
		return "redirect:/secure/admin/users";
	}
	
	@RequestMapping("/secure/admin/users/reset")
	public String usersReset(@ModelAttribute("userManagementForm") UserManagementForm form) {
		form.reset();
		return "redirect:/secure/admin/users";
	}

	@RequestMapping(value = "/secure/admin/users/new", method = RequestMethod.GET)
	public String newUser(@ModelAttribute("personForm") PersonForm form) {
		return "admin_user.page";
	}

	private String post(Person person, PersonForm form, Errors errors) {
		if (form.getPassword1() != null && !form.getPassword1().equals(form.getPassword2())) {
			errors.rejectValue("password1", "error.passwordNoMatch");
		}
		if (form.getType() != null && form.getType() == PersonType.COMPANY) {
			if (form.getCompanyName() == null || form.getCompanyName().isEmpty()) {
				errors.rejectValue("companyName", "NotEmpty");
			}
			if (form.getCompanyNumber() == null || form.getCompanyNumber().isEmpty()) {
				errors.rejectValue("companyNumber", "NotEmpty");
			}
		}
		if (form.getEmail() != null && !form.getEmail().isEmpty()
				&& !form.getEmail().equals(person.getEmail())
				&& personDao.findByEmail(form.getEmail()) != null) {
			errors.rejectValue("email", "InUse");
		}
		if (errors.hasErrors()) {
			return "admin_user.page";
		}
		if (form.getType() == PersonType.COMPANY) {
			Company co = companyDao.getByName(form.getCompanyName());
			if (co != null && !co.equals(person.getCompany())) {
				errors.rejectValue("companyName", "InUse");
			}
			co = companyDao.getByNumber(form.getCompanyNumber());
			if (co != null && !co.equals(person.getCompany())) {
				errors.rejectValue("companyNumber", "InUse");
			}
		}
		if (errors.hasErrors()) {
			return "admin_user.page";
		}
		person.setFirstName(form.getFirstName());
		person.setLastName(form.getLastName());
		person.setEmail(form.getEmail());
		person.setType(form.getType());
		if (form.getPassword1() != null && !form.getPassword1().isEmpty()) {
			person.setPassword(form.getPassword1());
		}
		personDao.saveOrUpdate(person);
		if (form.getType() == PersonType.COMPANY) {
			Company c = person.getCompany();
			if (c == null) {
				c = new Company();
			}
			c.setName(form.getCompanyName());
			c.setCompanyNumber(form.getCompanyNumber());
			c.setPerson(person);
			companyDao.saveOrUpdate(c);
		}
		if (form.getType() == PersonType.DRIVER) {
			Driver d = person.getDriver();
			if (d == null) {
				d = new Driver();
			}
			d.setPerson(person);
			driverDao.saveOrUpdate(d);
		}
		return "redirect:/secure/admin/users?message=success";
	}

	@RequestMapping(value = "/secure/admin/users/new", method = RequestMethod.POST)
	public String newUserPost(@ModelAttribute("personForm") @Valid PersonForm form, Errors errors) {
		return post(new Person(), form, errors);
	}

	@RequestMapping(value = "/secure/admin/users/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@ModelAttribute("personForm") PersonForm form, @PathVariable Long id) {
		Person person = personDao.find(id);
		form.setFirstName(person.getFirstName());
		form.setLastName(person.getLastName());
		form.setEmail(person.getEmail());
		form.setType(person.getType());
		if (person.getType() == PersonType.COMPANY) {
			Company co = companyDao.getCompany(person);
			if (co != null) {
				form.setCompanyName(co.getName());
				form.setCompanyNumber(co.getCompanyNumber());
			}
		}
		return new ModelAndView("admin_user.page", "person", person);
	}

	@RequestMapping(value = "/secure/admin/users/{id}", method = RequestMethod.POST)
	public ModelAndView editUserPost(@ModelAttribute("personForm") @Valid PersonForm form, Errors errors, @PathVariable Long id) {
		Person person = personDao.find(id);
		return new ModelAndView(post(person, form, errors), "person", person);
	}

	@RequestMapping(value = "/secure/admin/users/{personID}/delete", method = RequestMethod.GET)
	public String deleteUser(@PathVariable Long personID) {
		personDao.delete(personDao.find(personID));
		return "redirect:/secure/admin/users?message=success";
	}
}
