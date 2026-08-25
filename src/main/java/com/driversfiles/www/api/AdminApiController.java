package com.driversfiles.www.api;

import com.driversfiles.www.core.dao.CompanyDao;
import com.driversfiles.www.core.dao.ContentNodeDao;
import com.driversfiles.www.core.dao.DataImportDao;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.ContentNode;
import com.driversfiles.www.core.data.DataImport;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import com.driversfiles.www.core.service.ImportType;
import com.driversfiles.www.quartz.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

	private final PersonDao personDao;
	private final CompanyDao companyDao;
	private final DriverDao driverDao;
	private final DataImportDao dataImportDao;
	private final ContentNodeDao contentDao;
	private final JobService jobService;
	private final PasswordEncoder passwordEncoder;

	public AdminApiController(PersonDao personDao, CompanyDao companyDao, DriverDao driverDao,
			DataImportDao dataImportDao, ContentNodeDao contentDao, JobService jobService,
			PasswordEncoder passwordEncoder) {
		this.personDao = personDao;
		this.companyDao = companyDao;
		this.driverDao = driverDao;
		this.dataImportDao = dataImportDao;
		this.contentDao = contentDao;
		this.jobService = jobService;
		this.passwordEncoder = passwordEncoder;
	}

	public record AdminUserDto(Long id, String uuid, String firstName, String lastName,
			String email, String type, String companyName, String companyNumber) {}
	public record UserSave(Long id, String firstName, String lastName, String email,
			String password, String type, String companyName, String companyNumber) {}

	public static final List<String> CONTENT_NODES =
			List.of("HOME", "FEATURES", "FAQ", "CONTACTUS", "SIGNUP");

	// ---- Users ----
	@GetMapping("/users")
	public Map<String, Object> users(
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String companyName,
			@RequestParam(required = false) String companyNumber,
			@RequestParam(defaultValue = "-1") int max) {
		PersonType pt = null;
		if (type != null && !type.isEmpty()) {
			pt = PersonType.valueOf(type);
		}
		List<Person> people = personDao.getPeople(firstName, lastName, email, pt,
				companyName, companyNumber, 0, max);
		return Map.of("users", people.stream().map(this::toUserDto).toList());
	}

	@GetMapping("/users/{id}")
	public AdminUserDto user(@PathVariable Long id) {
		return toUserDto(personDao.find(id));
	}

	@PostMapping("/users")
	public AdminUserDto createUser(@RequestBody UserSave body) {
		Person p = new Person();
		applyAndValidate(p, body, true);
		personDao.saveOrUpdate(p);
		saveProfiles(p, body);
		return toUserDto(p);
	}

	@PutMapping("/users/{id}")
	public AdminUserDto updateUser(@PathVariable Long id, @RequestBody UserSave body) {
		Person p = personDao.find(id);
		applyAndValidate(p, body, false);
		if (body.password() != null && !body.password().isEmpty()) {
			p.setPassword(body.password());
		}
		personDao.saveOrUpdate(p);
		saveProfiles(p, body);
		return toUserDto(personDao.get(id));
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable Long id) {
		personDao.delete(personDao.find(id));
	}

	private void applyAndValidate(Person p, UserSave b, boolean isNew) {
		if (b.firstName() == null || b.lastName() == null || b.email() == null
				|| b.type() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
		}
		if (isNew && (b.password() == null || b.password().isEmpty())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
		}
		PersonType type = PersonType.valueOf(b.type());
		if (!b.email().equalsIgnoreCase(p.getEmail()) && personDao.findByEmail(b.email()) != null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
		}
		if (type == PersonType.COMPANY) {
			if (isBlank(b.companyName()) || isBlank(b.companyNumber())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Company name and number are required for company accounts");
			}
			Company byName = companyDao.getByName(b.companyName());
			if (byName != null && !byName.equals(p.getCompany())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already in use");
			}
			Company byNumber = companyDao.getByNumber(b.companyNumber());
			if (byNumber != null && !byNumber.equals(p.getCompany())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Company number already in use");
			}
		}
		p.setFirstName(b.firstName());
		p.setLastName(b.lastName());
		p.setEmail(b.email());
		p.setType(type);
		if (b.password() != null && !b.password().isEmpty()) {
			p.setPassword(b.password());
		}
	}

	private void saveProfiles(Person p, UserSave b) {
		if (p.getType() == PersonType.COMPANY) {
			Company c = p.getCompany();
			if (c == null) c = new Company();
			c.setName(b.companyName());
			c.setCompanyNumber(b.companyNumber());
			c.setPerson(p);
			companyDao.saveOrUpdate(c);
		} else if (p.getType() == PersonType.DRIVER) {
			Driver d = p.getDriver();
			if (d == null) d = new Driver();
			d.setPerson(p);
			driverDao.saveOrUpdate(d);
		}
	}

	private AdminUserDto toUserDto(Person p) {
		Company c = p.getCompany();
		return new AdminUserDto(p.getId(), p.getUuid(), p.getFirstName(), p.getLastName(),
				p.getEmail(), p.getType() != null ? p.getType().name() : null,
				c != null ? c.getName() : null, c != null ? c.getCompanyNumber() : null);
	}

	private boolean isBlank(String s) {
		return s == null || s.isBlank();
	}

	// ---- Data imports ----
	public record ImportDto(Long id, String importType, String companyName, Boolean overwrite,
			Boolean success, Date startTime, Date endTime, Date createdDate) {}

	@GetMapping("/imports/meta")
	public Map<String, Object> importsMeta() {
		return Map.of(
				"types", List.of(ImportType.values()),
				"companies", companyDao.getCompanies().stream()
						.map(c -> Map.of("id", c.getId(), "name", c.getName())).toList());
	}

	@GetMapping("/imports")
	public List<ImportDto> imports() {
		return dataImportDao.find("createdDate", false).stream().map(this::toImportDto).toList();
	}

	@PostMapping("/imports")
	public ImportDto createImport(@RequestParam("file") MultipartFile file,
			@RequestParam("importType") String importType,
			@RequestParam("companyId") Long companyId,
			@RequestParam(value = "overwrite", defaultValue = "false") boolean overwrite)
			throws IOException {
		DataImport di = new DataImport();
		di.setImportType(ImportType.valueOf(importType));
		di.setCompany(companyDao.find(companyId));
		di.setData(new String(file.getBytes()));
		di.setOvewrite(overwrite);
		dataImportDao.save(di);
		dataImportDao.flush();
		jobService.execute("dataImportJob");
		return toImportDto(di);
	}

	@DeleteMapping("/imports/{id}")
	public void deleteImport(@PathVariable Long id) {
		dataImportDao.delete(dataImportDao.find(id));
	}

	private ImportDto toImportDto(DataImport di) {
		return new ImportDto(di.getId(),
				di.getImportType() != null ? di.getImportType().name() : null,
				di.getCompany() != null ? di.getCompany().getName() : null,
				di.isOvewrite(), di.getSuccess(), di.getStartTime(), di.getEndTime(),
				di.getCreatedDate());
	}

	// ---- CMS content ----
	@GetMapping("/content")
	public List<Map<String, String>> contentNames() {
		return CONTENT_NODES.stream().map((n) -> Map.of("name", n)).toList();
	}

	@GetMapping("/content/{name}")
	public Map<String, String> contentNode(@PathVariable String name) {
		ContentNode cn = contentDao.findByName(name);
		return Map.of("name", name, "content", cn != null && cn.getContent() != null ? cn.getContent() : "");
	}

	@PutMapping("/content/{name}")
	public void updateContent(@PathVariable String name, @RequestBody Map<String, String> body) {
		ContentNode cn = contentDao.findByName(name);
		if (cn == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown content node");
		cn.setContent(body.getOrDefault("content", ""));
		contentDao.update(cn);
	}
}
