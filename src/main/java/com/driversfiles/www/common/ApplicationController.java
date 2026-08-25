package com.driversfiles.www.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.dao.AccidentDao;
import com.driversfiles.www.core.dao.ApplicationAccessDao;
import com.driversfiles.www.core.dao.CompanyDriverDao;
import com.driversfiles.www.core.dao.DocumentDao;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.dao.EmploymentDao;
import com.driversfiles.www.core.dao.LicenseDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.dao.ResidenceDao;
import com.driversfiles.www.core.dao.TrafficDao;
import com.driversfiles.www.core.dao.TruckDao;
import com.driversfiles.www.core.data.Accident;
import com.driversfiles.www.core.data.ApplicationAccess;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.CompanyDriver;
import com.driversfiles.www.core.data.Document;
import com.driversfiles.www.core.data.DocumentType;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Employment;
import com.driversfiles.www.core.data.License;
import com.driversfiles.www.core.data.LicenseType;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import com.driversfiles.www.core.data.Residence;
import com.driversfiles.www.core.data.Traffic;
import com.driversfiles.www.core.data.Truck;
import com.driversfiles.www.fs.FileStoreService;

/**
 * Handles generating an HTML based application for drivers to print/view/submit to companies.
 *
 * @author Erik R. Jensen
 * @author James Albright
 * @author Mark Burns
 */
@Controller
public class ApplicationController {
	
	@Autowired
	@Qualifier("accidentDao")
	private AccidentDao accidentDao;
	
	@Autowired
	@Qualifier("applicationAccessDao")
	private ApplicationAccessDao applicationAccessDao;
	
	@Autowired
	@Qualifier("authService")
	private AuthService authService;
	
	@Autowired
	@Qualifier("companyDriverDao")
	private CompanyDriverDao companyDriverDao;
	
	@Autowired
	@Qualifier("documentDao")
	private DocumentDao documentDao;

	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	@Autowired
	@Qualifier("employmentDao")
	private EmploymentDao employmentDao;
	
	@Autowired
	private FileStoreService fileStoreService;

	@Autowired
	@Qualifier("licenseDao")
	private LicenseDao licenseDao;

	@Autowired
	@Qualifier("personDao")
	private PersonDao personDao;

	@Autowired
	@Qualifier("residenceDao")
	private ResidenceDao residenceDao;
	
	@Autowired
	@Qualifier("trafficDao")
	private TrafficDao trafficDao;

	@Autowired
	@Qualifier("truckDao")
	private TruckDao truckDao;

	@InitBinder
	public void binder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@ModelAttribute("licensetypes")
	LicenseType[] getLicenseTypes() {
		return LicenseType.values();
	}

	@ModelAttribute("today")
	Date getToday() {
		return new Date();
	}

	@RequestMapping(value = "/secure/application/{uuid}", method = RequestMethod.GET)
	public String application(HttpServletRequest req, @PathVariable String uuid) {
		
		Person person = personDao.findByUuid(uuid);
		List<Truck> activeTrucks = truckDao.getTrucks(person, true);
		Truck truck = null;
		if (activeTrucks.size() > 1) {
			return "redirect:/secure/application/" + uuid + "/picktruck";
		} else if (activeTrucks.size() == 1) {
			truck = activeTrucks.get(0);
		}
		
		if (person != null && person.getType() == PersonType.DRIVER) {
			Driver driver = driverDao.getDriver(person);
			getApplicationComponents(driver, truck, req);
		}
		
		return "application.page";
	}
	
	@RequestMapping(value = "/secure/application/{uuid}/picktruck", method = RequestMethod.GET)
	public String applicationPickTruck(@PathVariable String uuid, 
			@ModelAttribute("personTruckForm") PersonTruckForm form, HttpServletRequest req) {
		
		Person person = personDao.findByUuid(uuid);
		req.setAttribute("person", person);
		if (person != null && person.getType() == PersonType.DRIVER) {
			Driver driver = driverDao.getDriver(person);
			req.setAttribute("driver", driver);
		}
		req.setAttribute("trucks", truckDao.getTrucks(person, true));
		form.setPersonUuid(uuid);
		
		return "pick_truck.page";
	}
	
	@RequestMapping(value = "/secure/application/picktruck", method = RequestMethod.POST)
	public String applicationPickTruckPost(@ModelAttribute("personTruckForm") @Valid PersonTruckForm form,
			Errors errors, HttpServletRequest req) {
		
		if (errors.hasErrors()) {
			return "pick_truck.page";
		}
		
		Person person = personDao.findByUuid(form.personUuid);
		Truck truck = truckDao.findByUuid(form.getTruckUuid());
		
		if (person != null && person.getType() == PersonType.DRIVER) {
			Driver driver = driverDao.getDriver(person);
			getApplicationComponents(driver, truck, req);
		}
		
		return "application.page";
	}
	
	public static class PersonTruckForm {
		
		@NotEmpty
		private String personUuid;
		@NotEmpty
		private String truckUuid;
		
		public String getPersonUuid() {
			return personUuid;
		}
		public void setPersonUuid(String personUuid) {
			this.personUuid = personUuid;
		}
		public String getTruckUuid() {
			return truckUuid;
		}
		public void setTruckUuid(String truckUuid) {
			this.truckUuid = truckUuid;
		}
	}
	
	@RequestMapping(value = "/application", method = RequestMethod.GET)
	public String accessApplication(@ModelAttribute("accessInformationForm") AccessInformationForm form) throws IOException {
		return "access_application_form.page";
	}
	
	@RequestMapping(value = "/application", method = RequestMethod.POST)
	public String accessApplicationPost(@ModelAttribute("accessInformationForm") @Valid AccessInformationForm form, 
			Errors errors, HttpServletRequest req) throws IOException {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "access_application_form.page";
		}
		
		// Try to get the driver
		Driver driver = driverDao.getDriverByAccessCode(form.getCode().toUpperCase());
		if (driver == null) {
			errors.rejectValue("code", "access.badcode");
			return "access_application_form.page";
		}
		
		List<Truck> activeTrucks = truckDao.getTrucks(driver.getPerson(), true);
		if (activeTrucks.size() > 1 && form.getTruckUuid() == null) {
			errors.rejectValue("truckUuid", "access.select.truck");
			req.setAttribute("trucks", activeTrucks);
			return "access_application_form.page";
		}
		
		Truck truck = null;
		if (form.getTruckUuid() != null) {
			truck = truckDao.findByUuid(form.getTruckUuid());
		} else if (activeTrucks.size() == 1) {
			truck = activeTrucks.get(0);
		}
		
		// Save the access data
		ApplicationAccess data = new ApplicationAccess();
		data.setName(form.getName());
		data.setEmail(form.getEmail());
		data.setCompany(form.getCompany());
		data.setDriver(driver);
		data.setCreatedDate(new Date());
		applicationAccessDao.save(data);
		
		// Get data for the display
		getApplicationComponents(driver, truck, req);
		
		return "application.page";
	}
	
	public static class AccessInformationForm {
		@NotEmpty
		private String name;
		@NotEmpty
		@Email
		private String email;
		@NotEmpty
		private String company;
		@NotEmpty
		private String code;
		private String truckUuid;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getTruckUuid() {
			return truckUuid;
		}
		public void setTruckUuid(String truckUuid) {
			this.truckUuid = truckUuid;
		}
	}

	private void getApplicationComponents(Driver driver, Truck activeTruck, HttpServletRequest req) {
		
		Person person = driver.getPerson();
		req.setAttribute("person", person);
		req.setAttribute("driver", driver);
		
		List<Residence> residences = residenceDao.getResidences(driver);
		req.setAttribute("residences", residences);
		
		Document cdlDoc = documentDao.getDocument(person, DocumentType.DOC_TYPE_CDL.getName());
		req.setAttribute("cdlDoc", cdlDoc);
		Document medCardDoc = documentDao.getDocument(person, DocumentType.DOC_TYPE_MED_CARD.getName());
		req.setAttribute("medCardDoc", medCardDoc);
		Document physicalDoc = documentDao.getDocument(person, DocumentType.DOC_TYPE_PHYSICAL.getName());
		req.setAttribute("physicalDoc", physicalDoc);
		Document ssCardDoc = documentDao.getDocument(person, DocumentType.DOC_TYPE_SS_CARD.getName());
		req.setAttribute("ssCardDoc", ssCardDoc);
		
		List<License> licenses = licenseDao.getLicenses(driver);
		req.setAttribute("licenses", licenses);
		
		List<Accident> accidents = accidentDao.getAccidents(driver);
		req.setAttribute("accidents", accidents);
		
		List<Traffic> traffics = trafficDao.getTrafficRecords(driver);
		req.setAttribute("traffics", traffics);
		
		List<Employment> employments = employmentDao.getEmployments(driver);
		req.setAttribute("employments", employments);
		
		if (activeTruck == null) {
			List<Truck> activeTrucks = truckDao.getTrucks(person, true);
			if (activeTrucks.size() > 0) {
				activeTruck = activeTrucks.get(0);
			}
		}
		req.setAttribute("activeTruck", activeTruck);
		
		Person authUser = authService.getEffectiveUser();
		if (authUser != null && authUser.getType() == PersonType.COMPANY) {
			Company company = authUser.getCompany();
			if (company != null) {
				req.setAttribute("company", company);
				String iconPath = "company/icon/" + company.getUuid();
				if (fileStoreService.exists(iconPath)) {
					String uri = String.format("/secure/company/%s/icon", company.getUuid());
					req.setAttribute("companyIconUrl", uri);
				}
			}
		}
	}
	
	
	@RequestMapping(value = "/secure/application/views", method = RequestMethod.GET)
	public String applicationViews(@ModelAttribute("appViewSearchForm") AppViewSearchForm form,
			HttpServletRequest req) throws IOException {
		
		// Based on user type get the views
		Person person = authService.getAuthenticatedUser();
		List<ApplicationAccess> appViews = null;
		if (person.getType() == PersonType.DRIVER) {
			List<Driver> drivers = new ArrayList<Driver>();
			drivers.add(person.getDriver());
			appViews  = applicationAccessDao.findByDrivers(drivers, 0, form.getMaxRecords());
			req.setAttribute("isDriver", true);
		} else if (person.getType() == PersonType.COMPANY) {
			Company company = person.getCompany();
			List<CompanyDriver> cdList = companyDriverDao.get(company);
			List<Driver> drivers = new ArrayList<Driver>();
			for (CompanyDriver cd : cdList) {
				drivers.add(cd.getDriver());
			}
			appViews = applicationAccessDao.findByDrivers(drivers, 0, form.getMaxRecords());
			req.setAttribute("isDriver", false);
		} else if (person.getType() == PersonType.ADMIN) {
			// Get all
			appViews = applicationAccessDao.find("email", true, 0, form.getMaxRecords());
			req.setAttribute("isDriver", false);
		}
		
		req.setAttribute("appViews", appViews);
		
		String type = "";
		if (person.getType() == PersonType.DRIVER)
			type = "driver_";
		else if (person.getType() == PersonType.COMPANY)
			type = "company_";
		else if (person.getType() == PersonType.ADMIN)
			type = "admin_";
			
		return type + "application_views.page";
	}
	
	@RequestMapping(value = "/secure/application/views", method = RequestMethod.POST)
	public String searchApplicationViews(@ModelAttribute("appViewSearchForm") @Valid AppViewSearchForm form, 
			Errors errors, HttpServletRequest req) throws IOException {
		
		if (req.getParameter("clear") != null) {
			form.clear();
		}
		
		List<Driver> drivers = new ArrayList<Driver>();
		if (form.getDriverEmail() != null && !form.getDriverEmail().isEmpty()) {
			Person person = personDao.findByEmail(form.getDriverEmail());
			if (person != null) {
				drivers.add(person.getDriver());
			}
		}
		
		// Based on user type get filtered list
		Person person = authService.getAuthenticatedUser();
		List<ApplicationAccess> appViews = null;
		if (person.getType() == PersonType.DRIVER) {
			drivers.add(person.getDriver());
			req.setAttribute("isDriver", true);
		} else if (person.getType() == PersonType.COMPANY) {
			Company company = person.getCompany();
			List<CompanyDriver> cdList = companyDriverDao.get(company);
			for (CompanyDriver cd : cdList) {
				drivers.add(cd.getDriver());
			}
			req.setAttribute("isDriver", true);
		} else if (person.getType() == PersonType.ADMIN) {
			req.setAttribute("isDriver", false);
		}
		
		appViews = applicationAccessDao.findByFilter(drivers, form.getViewerEmail(),
				form.getViewerCompany(), form.getStartDate(), form.getEndDate(), 
				0, form.getMaxRecords());
		req.setAttribute("appViews", appViews);

		String type = "";
		if (person.getType() == PersonType.DRIVER)
			type = "driver_";
		else if (person.getType() == PersonType.COMPANY)
			type = "company_";
		else if (person.getType() == PersonType.ADMIN)
			type = "admin_";
			
		return type + "application_views.page";
	}
	
	public static class AppViewSearchForm {
		
		@Email
		private String driverEmail;
		@Email
		private String viewerEmail;
		private String viewerCompany;
		private Date startDate;
		private Date endDate;
		private Integer maxRecords = 10;
		
		public void clear() {
			driverEmail = null;
			viewerEmail = null;
			viewerCompany = null;
			startDate = null;
			endDate = null;
			maxRecords = 10;
		}
		
		public String getDriverEmail() {
			return driverEmail;
		}
		public void setDriverEmail(String driverEmail) {
			this.driverEmail = driverEmail;
		}
		public String getViewerEmail() {
			return viewerEmail;
		}
		public void setViewerEmail(String viewerEmail) {
			this.viewerEmail = viewerEmail;
		}
		public String getViewerCompany() {
			return viewerCompany;
		}
		public void setViewerCompany(String viewerCompany) {
			this.viewerCompany = viewerCompany;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}

		public Integer getMaxRecords() {
			return maxRecords;
		}

		public void setMaxRecords(Integer maxRecords) {
			this.maxRecords = maxRecords;
		}
	}
}
