package com.driversfiles.www.api;

import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.dao.LicenseDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.License;
import com.driversfiles.www.core.data.LicenseType;
import com.driversfiles.www.core.data.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/driver/me")
public class DriverDataApiController {

	private final PersonDao personDao;
	private final LicenseDao licenseDao;

	public DriverDataApiController(PersonDao personDao, LicenseDao licenseDao) {
		this.personDao = personDao;
		this.licenseDao = licenseDao;
	}

	public record PersonalInfo(String uuid, String firstName, String middleName,
			String lastName, String email) {}
	public record PersonalInfoUpdate(String firstName, String middleName, String lastName) {}

	public record LicenseDto(String uuid, String state, String number, String type,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date expiration,
			boolean current) {}
	public record LicenseCreate(String state, String number, String type,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date expiration,
			boolean current) {}

	private Person currentUser(Authentication auth) {
		Person p = personDao.findByEmail(auth.getName());
		if (p == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		return p;
	}

	private Driver requireDriver(Person p) {
		Driver d = p.getDriver();
		if (d == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authenticated user is not a driver");
		}
		return d;
	}

	@GetMapping("/personal-info")
	public PersonalInfo personalInfo(Authentication auth) {
		Person p = currentUser(auth);
		return new PersonalInfo(p.getUuid(), p.getFirstName(), p.getMiddleName(),
				p.getLastName(), p.getEmail());
	}

	@PutMapping("/personal-info")
	public PersonalInfo updatePersonalInfo(Authentication auth, @RequestBody PersonalInfoUpdate body) {
		Person p = currentUser(auth);
		if (body.firstName() != null) p.setFirstName(body.firstName());
		if (body.middleName() != null) p.setMiddleName(body.middleName());
		if (body.lastName() != null) p.setLastName(body.lastName());
		personDao.update(p);
		return new PersonalInfo(p.getUuid(), p.getFirstName(), p.getMiddleName(),
				p.getLastName(), p.getEmail());
	}

	@GetMapping("/licenses")
	public List<LicenseDto> licenses(Authentication auth) {
		Driver driver = requireDriver(currentUser(auth));
		return licenseDao.getLicenses(driver).stream()
				.map(this::toDto).toList();
	}

	@PostMapping("/licenses")
	public LicenseDto addLicense(Authentication auth, @RequestBody LicenseCreate body) {
		Driver driver = requireDriver(currentUser(auth));
		LicenseType type;
		try {
			type = LicenseType.valueOf(body.type());
		} catch (IllegalArgumentException x) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown license type: " + body.type());
		}
		License l = new License();
		l.setDriver(driver);
		l.setState(body.state());
		l.setNumber(body.number());
		l.setType(type);
		l.setExpiration(body.expiration());
		l.setCurrent(body.current());
		licenseDao.save(l);
		return toDto(l);
	}

	@DeleteMapping("/licenses/{uuid}")
	public void deleteLicense(Authentication auth, @PathVariable String uuid) {
		Driver driver = requireDriver(currentUser(auth));
		License l = licenseDao.getByUuid(uuid);
		if (l == null || !driver.getId().equals(l.getDriver().getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		licenseDao.delete(l);
	}

	private LicenseDto toDto(License l) {
		return new LicenseDto(l.getUuid(), l.getState(), l.getNumber(),
				l.getType() != null ? l.getType().name() : null,
				l.getExpiration(), l.isCurrent());
	}
}
