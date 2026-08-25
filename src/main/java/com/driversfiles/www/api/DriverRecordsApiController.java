package com.driversfiles.www.api;

import com.driversfiles.www.core.dao.AccidentDao;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.dao.EmploymentDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.dao.ResidenceDao;
import com.driversfiles.www.core.dao.TrafficDao;
import com.driversfiles.www.core.data.Accident;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Employment;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.Residence;
import com.driversfiles.www.core.data.Traffic;
import com.driversfiles.www.core.service.AccessCodeService;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/driver/me")
public class DriverRecordsApiController {

	private final PersonDao personDao;
	private final ResidenceDao residenceDao;
	private final EmploymentDao employmentDao;
	private final AccidentDao accidentDao;
	private final TrafficDao trafficDao;
	private final DriverDao driverDao;
	private final AccessCodeService accessCodeService;

	public DriverRecordsApiController(PersonDao personDao, ResidenceDao residenceDao,
			EmploymentDao employmentDao, AccidentDao accidentDao, TrafficDao trafficDao,
			DriverDao driverDao, AccessCodeService accessCodeService) {
		this.personDao = personDao;
		this.residenceDao = residenceDao;
		this.employmentDao = employmentDao;
		this.accidentDao = accidentDao;
		this.trafficDao = trafficDao;
		this.driverDao = driverDao;
		this.accessCodeService = accessCodeService;
	}

	public record ResidenceDto(String uuid, String address1, String address2, String city,
			String state, String postalCode) {}
	public record EmploymentDto(String uuid, String name, String supervisor, String address,
			String city, String state, String postalCode, String phone, String position,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date fromDate,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date toDate,
			String leaving) {}
	public record AccidentDto(String uuid,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date accidentDate,
			String type, String nature, Boolean atFault, Boolean fatalities, Boolean injuries,
			BigDecimal damages) {}
	public record TrafficDto(String uuid,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date trafficDate,
			String city, String state, String charge, String penalty) {}
	public record AccessCodeInfo(String accessCode, Date createdDate) {}

	private Person currentUser(Authentication auth) {
		Person p = personDao.findByEmail(auth.getName());
		if (p == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		return p;
	}

	private Driver requireDriver(Authentication auth) {
		Driver d = currentUser(auth).getDriver();
		if (d == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a driver");
		return d;
	}

	private <T> List<T> listFor(Function<Driver, List<T>> query, Authentication auth) {
		return query.apply(requireDriver(auth));
	}

	private <T> T ownedRecord(Function<String, T> byUuid, Function<T, Driver> owner,
			Authentication auth, String uuid) {
		T record = byUuid.apply(uuid);
		if (record == null || !requireDriver(auth).getId().equals(owner.apply(record).getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return record;
	}

	// ---- Residences ----
	@GetMapping("/residences")
	public List<ResidenceDto> residences(Authentication auth) {
		return listFor(residenceDao::getResidences, auth).stream()
				.map(r -> new ResidenceDto(r.getUuid(), r.getAddress1(), r.getAddress2(),
						r.getCity(), r.getState(), r.getPostalCode())).toList();
	}

	@PostMapping("/residences")
	public ResidenceDto addResidence(Authentication auth, @RequestBody ResidenceDto body) {
		Driver d = requireDriver(auth);
		Residence r = new Residence();
		r.setDriver(d);
		applyResidence(r, body);
		residenceDao.save(r);
		return toResidenceDto(r.getUuid());
	}

	@PutMapping("/residences/{uuid}")
	public ResidenceDto updateResidence(Authentication auth, @PathVariable String uuid,
			@RequestBody ResidenceDto body) {
		Residence r = ownedRecord(residenceDao::getByUuid, Residence::getDriver, auth, uuid);
		applyResidence(r, body);
		residenceDao.update(r);
		return toResidenceDto(uuid);
	}

	@DeleteMapping("/residences/{uuid}")
	public void deleteResidence(Authentication auth, @PathVariable String uuid) {
		residenceDao.delete(ownedRecord(residenceDao::getByUuid, Residence::getDriver, auth, uuid));
	}

	private void applyResidence(Residence r, ResidenceDto b) {
		r.setAddress1(b.address1()); r.setAddress2(b.address2());
		r.setCity(b.city()); r.setState(b.state()); r.setPostalCode(b.postalCode());
	}

	private ResidenceDto toResidenceDto(String uuid) {
		Residence r = residenceDao.getByUuid(uuid);
		return new ResidenceDto(r.getUuid(), r.getAddress1(), r.getAddress2(),
				r.getCity(), r.getState(), r.getPostalCode());
	}

	// ---- Employments ----
	@GetMapping("/employments")
	public List<EmploymentDto> employments(Authentication auth) {
		return listFor(employmentDao::getEmployments, auth).stream()
				.map(e -> new EmploymentDto(e.getUuid(), e.getName(), e.getSupervisor(), e.getAddress(),
						e.getCity(), e.getState(), e.getPostalCode(), e.getPhone(), e.getPosition(),
						e.getFromDate(), e.getToDate(), e.getLeaving())).toList();
	}

	@PostMapping("/employments")
	public EmploymentDto addEmployment(Authentication auth, @RequestBody EmploymentDto body) {
		Employment e = new Employment();
		e.setDriver(requireDriver(auth));
		applyEmployment(e, body);
		employmentDao.save(e);
		return firstEmploymentDto(e.getUuid());
	}

	@DeleteMapping("/employments/{uuid}")
	public void deleteEmployment(Authentication auth, @PathVariable String uuid) {
		employmentDao.delete(ownedRecord(employmentDao::getByUuid, Employment::getDriver, auth, uuid));
	}

	private void applyEmployment(Employment e, EmploymentDto b) {
		e.setName(b.name()); e.setSupervisor(b.supervisor()); e.setAddress(b.address());
		e.setCity(b.city()); e.setState(b.state()); e.setPostalCode(b.postalCode());
		e.setPhone(b.phone()); e.setPosition(b.position()); e.setFromDate(b.fromDate());
		e.setToDate(b.toDate()); e.setLeaving(b.leaving());
	}

	private EmploymentDto firstEmploymentDto(String uuid) {
		Employment e = employmentDao.getByUuid(uuid);
		return new EmploymentDto(e.getUuid(), e.getName(), e.getSupervisor(), e.getAddress(),
				e.getCity(), e.getState(), e.getPostalCode(), e.getPhone(), e.getPosition(),
				e.getFromDate(), e.getToDate(), e.getLeaving());
	}

	// ---- Accidents ----
	@GetMapping("/accidents")
	public List<AccidentDto> accidents(Authentication auth) {
		return listFor(accidentDao::getAccidents, auth).stream()
				.map(a -> new AccidentDto(a.getUuid(), a.getAccidentDate(), a.getType(),
						a.getNature(), a.getAtFault(), a.getFatalities(), a.getInjuries(),
						a.getDamages())).toList();
	}

	@PostMapping("/accidents")
	public AccidentDto addAccident(Authentication auth, @RequestBody AccidentDto body) {
		Accident a = new Accident();
		a.setDriver(requireDriver(auth));
		a.setAccidentDate(body.accidentDate());
		a.setType(body.type()); a.setNature(body.nature());
		a.setAtFault(body.atFault()); a.setFatalities(body.fatalities());
		a.setInjuries(body.injuries()); a.setDamages(body.damages());
		accidentDao.save(a);
		a = accidentDao.getByUuid(a.getUuid());
		return new AccidentDto(a.getUuid(), a.getAccidentDate(), a.getType(), a.getNature(),
				a.getAtFault(), a.getFatalities(), a.getInjuries(), a.getDamages());
	}

	@DeleteMapping("/accidents/{uuid}")
	public void deleteAccident(Authentication auth, @PathVariable String uuid) {
		accidentDao.delete(ownedRecord(accidentDao::getByUuid, Accident::getDriver, auth, uuid));
	}

	// ---- Traffic convictions ----
	@GetMapping("/traffics")
	public List<TrafficDto> traffics(Authentication auth) {
		return listFor(trafficDao::getTrafficRecords, auth).stream()
				.map(t -> new TrafficDto(t.getUuid(), t.getTrafficDate(), t.getCity(),
						t.getState(), t.getCharge(), t.getPenalty())).toList();
	}

	@PostMapping("/traffics")
	public TrafficDto addTraffic(Authentication auth, @RequestBody TrafficDto body) {
		Traffic t = new Traffic();
		t.setDriver(requireDriver(auth));
		t.setTrafficDate(body.trafficDate());
		t.setCity(body.city()); t.setState(body.state());
		t.setCharge(body.charge()); t.setPenalty(body.penalty());
		trafficDao.save(t);
		t = trafficDao.getByUuid(t.getUuid());
		return new TrafficDto(t.getUuid(), t.getTrafficDate(), t.getCity(), t.getState(),
				t.getCharge(), t.getPenalty());
	}

	@DeleteMapping("/traffics/{uuid}")
	public void deleteTraffic(Authentication auth, @PathVariable String uuid) {
		trafficDao.delete(ownedRecord(trafficDao::getByUuid, Traffic::getDriver, auth, uuid));
	}

	// ---- Access code ----
	@GetMapping("/access-code")
	public AccessCodeInfo accessCode(Authentication auth) {
		Driver d = requireDriver(auth);
		if (d.getAccessCode() == null || isExpired(d)) {
			accessCodeService.generateNewAccessCode(d);
		}
		driverDao.evict(d);
		Driver fresh = driverDao.get(d.getId());
		return new AccessCodeInfo(fresh.getAccessCode(), fresh.getAccessCodeExpireDate());
	}

	private boolean isExpired(Driver d) {
		return d.getAccessCodeExpireDate() == null
				|| d.getAccessCodeExpireDate().before(new Date());
	}
}
