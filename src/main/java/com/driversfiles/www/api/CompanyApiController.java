package com.driversfiles.www.api;

import com.driversfiles.www.core.dao.ApplicationAccessDao;
import com.driversfiles.www.core.dao.CompanyDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.ApplicationAccess;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/company/me")
public class CompanyApiController {

	private final PersonDao personDao;
	private final CompanyDao companyDao;
	private final ApplicationAccessDao applicationAccessDao;

	public CompanyApiController(PersonDao personDao, CompanyDao companyDao,
			ApplicationAccessDao applicationAccessDao) {
		this.personDao = personDao;
		this.companyDao = companyDao;
		this.applicationAccessDao = applicationAccessDao;
	}

	public record CompanyProfile(String uuid, String name, String companyNumber, String address1,
			String address2, String city, String state, String postalCode, String phone,
			String fax, String website) {}
	public record CompanyUpdate(String name, String address1, String address2, String city,
			String state, String postalCode, String phone, String fax, String website) {}
	public record GrantedDriver(String email, String name, String company,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date grantedDate) {}

	private Company currentCompany(Authentication auth) {
		Person p = personDao.findByEmail(auth.getName());
		if (p == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		Company c = p.getCompany();
		if (c == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a company user");
		return c;
	}

	@GetMapping("/profile")
	public CompanyProfile profile(Authentication auth) {
		return toDto(currentCompany(auth));
	}

	@PutMapping("/profile")
	public CompanyProfile updateProfile(Authentication auth, @RequestBody CompanyUpdate body) {
		Company c = currentCompany(auth);
		if (body.name() != null && !body.name().isBlank()) c.setName(body.name());
		c.setAddress1(body.address1()); c.setAddress2(body.address2());
		c.setCity(body.city()); c.setState(body.state()); c.setPostalCode(body.postalCode());
		c.setPhone(body.phone()); c.setFax(body.fax()); c.setWebsite(body.website());
		companyDao.update(c);
		return toDto(c);
	}

	@GetMapping("/drivers")
	public List<GrantedDriver> drivers(Authentication auth) {
		Company c = currentCompany(auth);
		Map<String, GrantedDriver> unique = new LinkedHashMap<>();
		for (ApplicationAccess a : applicationAccessDao.find("createdDate", false)) {
			boolean matches = (a.getCompany() != null && a.getCompany().equalsIgnoreCase(c.getName()))
					|| (a.getEmail() != null && a.getEmail().equalsIgnoreCase(
							c.getPerson() != null ? c.getPerson().getEmail() : ""));
			if (!matches || a.getDriver() == null) continue;
			Person dp = a.getDriver().getPerson();
			String key = dp != null ? dp.getUuid() : a.getEmail();
			unique.putIfAbsent(key, new GrantedDriver(
					dp != null ? dp.getEmail() : a.getEmail(),
					dp != null ? dp.getFirstName() + " " + dp.getLastName() : a.getName(),
					a.getCompany(), a.getCreatedDate()));
		}
		return List.copyOf(unique.values());
	}

	private CompanyProfile toDto(Company c) {
		return new CompanyProfile(c.getUuid(), c.getName(), c.getCompanyNumber(), c.getAddress1(),
				c.getAddress2(), c.getCity(), c.getState(), c.getPostalCode(), c.getPhone(),
				c.getFax(), c.getWebsite());
	}
}
