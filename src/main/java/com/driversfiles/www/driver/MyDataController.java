package com.driversfiles.www.driver;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.controller.BaseController;
import com.driversfiles.www.core.dao.AccidentDao;
import com.driversfiles.www.core.dao.DocumentDao;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.dao.EmploymentDao;
import com.driversfiles.www.core.dao.LicenseDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.dao.ResidenceDao;
import com.driversfiles.www.core.dao.TrafficDao;
import com.driversfiles.www.core.data.Accident;
import com.driversfiles.www.core.data.Document;
import com.driversfiles.www.core.data.DocumentType;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.Employment;
import com.driversfiles.www.core.data.License;
import com.driversfiles.www.core.data.LicenseType;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import com.driversfiles.www.core.data.Residence;
import com.driversfiles.www.core.data.State;
import com.driversfiles.www.core.data.Traffic;
import com.driversfiles.www.fs.FileStoreService;
import com.driversfiles.www.spring.ValidationUtils;

/**
 * Handles My Data requests.
 *
 * @author Erik R. Jensen
 * @author Mark Burns
 */
@Controller
public class MyDataController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(MyDataController.class);
	
	@Autowired
	private AccidentDao accidentDao;
	
	@Autowired
	private AuthService authService;

	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private DriverDao driverDao;

	@Autowired
	private EmploymentDao employmentDao;
	
	@Autowired
	private FileStoreService fileStoreService;

	@Autowired
	private LicenseDao licenseDao;

	@Autowired
	private PersonDao personDao;

	@Autowired
	private ResidenceDao residenceDao;
	
	@Autowired
	private TrafficDao trafficDao;

	@InitBinder
	public void binder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@ModelAttribute("states")
	State[] getStates() {
		return State.values();
	}

	@ModelAttribute("licensetypes")
	LicenseType[] getLicenseTypes() {
		return LicenseType.values();
	}

	@RequestMapping(value = "/secure/driver/mydata/personal_information", method = RequestMethod.GET)
	public String personalInformation(@ModelAttribute("personalInformationForm") PersonalInformationForm form, HttpServletRequest req) {

		// Prefill with driver info if it exists
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER) {
			Driver driver = person.getDriver();
			if (driver != null) {
				form.setFirstName(person.getFirstName());
				form.setMiddleName(person.getMiddleName());
				form.setLastName(person.getLastName());
				form.setEmail(person.getEmail());
				form.setDob(driver.getDob());
				form.setSsn(driver.getSsn());
				form.setPhone(driver.getPhone());
				form.setMobile(driver.getMobile());
				form.setFax(driver.getFax());
				form.setAddress1(driver.getAddress1());
				form.setAddress2(driver.getAddress2());
				form.setCity(driver.getCity());
				form.setState(driver.getState());
				form.setPostalCode(driver.getPostalCode());
			}
		}

		return "personal_information.page";
	}

	@RequestMapping(value = "/secure/driver/mydata/personal_information", method = RequestMethod.POST)
	public String personalInformationPost(@ModelAttribute("personalInformationForm") @Valid PersonalInformationForm form,
			Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "personal_information.page";
		}
		
		if (form.getDob() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(form.getDob());
			ValidationUtils.rejectInvalidYear(cal.get(Calendar.YEAR), errors, "dob", "error.invalid.year", 1900, null);
		}
		if (errors.hasErrors()) {
			return "personal_information.page";
		}
		
		// Save the data
		Person person = authService.getEffectiveUser();
		person.setFirstName(form.getFirstName());
		person.setMiddleName(form.getMiddleName());
		person.setLastName(form.getLastName());
		person.setEmail(form.getEmail());
		personDao.update(person);
		
		Driver driver = driverDao.getDriver(person);
		if (driver == null && person.getType() == PersonType.DRIVER) {
			driver = createNewDriver(person);
		}
		if (driver != null) {
			driver.setDob(form.getDob());
			driver.setSsn(form.getSsn());
			driver.setPhone(form.getPhone());
			driver.setMobile(form.getMobile());
			driver.setFax(form.getFax());
			driver.setAddress1(form.getAddress1());
			driver.setAddress2(form.getAddress2());
			driver.setCity(form.getCity());
			driver.setState(form.getState());
			driver.setPostalCode(form.getPostalCode());
			driverDao.update(driver);
		}

		// Handle Save/Next
		if (req.getParameter("next") != null) {
			return "redirect:/secure/driver/mydata/emergency_contact?message=success";
		} else {
			return "redirect:/secure/driver/mydata/personal_information?message=success";
		}
	}

	public static class PersonalInformationForm {
		@NotEmpty
		private String firstName;
		private String middleName;
		@NotEmpty
		private String lastName;
		@NotEmpty
		@Email
		private String email;
		private Date dob;
		private String ssn;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String phone;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String mobile;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String fax;
		@NotEmpty
		private String address1;
		private String address2;
		@NotEmpty
		private String city;
		@NotEmpty
		private String state;
		@NotEmpty
		private String postalCode;

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

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Date getDob() {
			return dob;
		}

		public void setDob(Date dob) {
			this.dob = dob;
		}

		public String getSsn() {
			return ssn;
		}

		public void setSsn(String ssn) {
			this.ssn = ssn;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getFax() {
			return fax;
		}

		public void setFax(String fax) {
			this.fax = fax;
		}

		public String getAddress1() {
			return address1;
		}

		public void setAddress1(String address1) {
			this.address1 = address1;
		}

		public String getAddress2() {
			return address2;
		}

		public void setAddress2(String address2) {
			this.address2 = address2;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
	}

	@RequestMapping(value = "/secure/driver/mydata/emergency_contact", method = RequestMethod.GET)
	public String emergencyContact(@ModelAttribute("emergencyContactForm") EmergencyContactForm form) {
		
		// Prefill form if it exists
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
			Driver driver = person.getDriver();
			form.setName(driver.getContactName());
			form.setRelationship(driver.getContactRelationship());
			form.setPhone(driver.getContactPhone());
			form.setMobile(driver.getContactMobile());
		}
		
		return "emergency_contact.page";
	}

	@RequestMapping(value = "/secure/driver/mydata/emergency_contact", method = RequestMethod.POST)
	public String emergencyContactPost(@ModelAttribute("emergencyContactForm") @Valid EmergencyContactForm form, Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "emergency_contact.page";
		}
		
		// Save the data
		Person person = authService.getEffectiveUser();
		Driver driver = driverDao.getDriver(person);
		if (driver == null && person.getType() == PersonType.DRIVER) {
			driver = createNewDriver(person);
		}
		
		if (driver != null) {
			driver.setContactName(form.getName());
			driver.setContactRelationship(form.getRelationship());
			driver.setContactPhone(form.getPhone());
			driver.setContactMobile(form.getMobile());
			driverDao.update(driver);
		}
		
		// Handle save/next
		if (req.getParameter("next") != null) {
			return "redirect:/secure/driver/mydata/documents?message=success";
		} else {
			return "emergency_contact.page";
		}
	}

	public static class EmergencyContactForm {

		private String name;
		private String relationship;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String phone;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String mobile;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRelationship() {
			return relationship;
		}

		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
	}

	@RequestMapping(value = "/secure/driver/mydata/documents", method = RequestMethod.GET)
	public String documents(@ModelAttribute("documentUploadForm") DocumentUploadForm form, HttpServletRequest req) {
		
		// Get any documents that this person has uploaded
		Person person = authService.getEffectiveUser();
		List<Document> docs = documentDao.getDocuments(person);
		if (docs != null && docs.size() > 0) {
			req.setAttribute("docs", docs);
		}
		req.setAttribute("docTypes", DocumentType.values());
		req.setAttribute("now", new Date());
		
		return "documents.page";
	}

	@RequestMapping(value = "/secure/driver/mydata/documents", method = RequestMethod.POST)
	public String documentsPost(@ModelAttribute("documentUploadForm") @Valid DocumentUploadForm form, Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "documents.page";
		}
		
		// Save files
		try {
			Person person = authService.getEffectiveUser();
			Date now = new Date();
			
			MultipartFile cdlFile = form.getCdlFile();
			if (cdlFile != null && cdlFile.getSize() > 0) {
				Date expire = form.getCdlExpireDate();
				if (expire == null) {
					errors.rejectValue("cdlExpireDate", "date.required");
					return "documents.page";
				}
				if (expire.before(now)) {
					errors.rejectValue("cdlExpireDate", "date.expired");
					return "documents.page";
				}
				
				String filename = genDocFilename(DocumentType.DOC_TYPE_CDL.name(), person.getId());
				fileStoreService.saveFile("documents/" + filename, cdlFile.getInputStream());
				Document doc = new Document(person, DocumentType.DOC_TYPE_CDL.name(), filename, null, expire);
				documentDao.save(doc);
			}
			
			MultipartFile medFile = form.getMedicalCardFile();
			if (medFile != null && medFile.getSize() > 0) {
				Date expire = form.getMedicalExpireDate();
				if (expire == null) {
					errors.rejectValue("medicalExpireDate", "date.required");
					return "documents.page";
				}
				if (expire.before(now)) {
					errors.rejectValue("medicalExpireDate", "date.expired");
					return "documents.page";
				}
				
				String filename = genDocFilename(DocumentType.DOC_TYPE_MED_CARD.name(), person.getId());
				fileStoreService.saveFile("documents/" + filename, medFile.getInputStream());
				Document doc = new Document(person, DocumentType.DOC_TYPE_MED_CARD.name(), filename, null, expire);
				documentDao.save(doc);
			}
			
			MultipartFile phyFile = form.getPhysicalFile();
			if (phyFile != null && phyFile.getSize() > 0) {
				Date expire = form.getPhysicalExpireDate();
				if (expire == null) {
					errors.rejectValue("physicalExpireDate", "date.required");
					return "documents.page";
				}
				if (expire.before(now)) {
					errors.rejectValue("physicalExpireDate", "date.expired");
					return "documents.page";
				}
				
				String filename = genDocFilename(DocumentType.DOC_TYPE_PHYSICAL.name(), person.getId());
				fileStoreService.saveFile("documents/" + filename, phyFile.getInputStream());
				Document doc = new Document(person, DocumentType.DOC_TYPE_PHYSICAL.name(), filename, null, expire);
				documentDao.save(doc);
			}
			
			MultipartFile ssFile = form.getSsCardFile();
			if (ssFile != null && ssFile.getSize() > 0) {
				String filename = genDocFilename(DocumentType.DOC_TYPE_SS_CARD.name(), person.getId());
				fileStoreService.saveFile("documents/" + filename, ssFile.getInputStream());
				Document doc = new Document(person, DocumentType.DOC_TYPE_SS_CARD.name(), filename, null, null);
				documentDao.save(doc);
			}
			
		} catch (IOException e) {
			log.error("Error in MyDataController.documentsPost", e);
			errors.reject(null, "There was a severe error while uploading the file(s)!");
			return "documents.page";
		}

		// Handle save/next
		if (req.getParameter("next") != null) {
			return "redirect:/secure/driver/mydata/residency_history?message=success";
		} else {
			return "redirect:/secure/driver/mydata/documents?message=success";
		}
	}
	
	private String genDocFilename(String type, Long id) {
		
		String filename = String.format("%s_FILE-%06d.%s", type, id, "pdf");
		
		return filename;
	}

	public static class DocumentUploadForm {
		
		private MultipartFile cdlFile;
		private MultipartFile ssCardFile;
		private MultipartFile physicalFile;
		private MultipartFile medicalCardFile;
		private Date cdlExpireDate;
		private Date physicalExpireDate;
		private Date medicalExpireDate;
		
		public MultipartFile getCdlFile() {
			return cdlFile;
		}
		public void setCdlFile(MultipartFile cdlFile) {
			this.cdlFile = cdlFile;
		}
		public MultipartFile getSsCardFile() {
			return ssCardFile;
		}
		public void setSsCardFile(MultipartFile ssCardFile) {
			this.ssCardFile = ssCardFile;
		}
		public MultipartFile getPhysicalFile() {
			return physicalFile;
		}
		public void setPhysicalFile(MultipartFile physicalFile) {
			this.physicalFile = physicalFile;
		}
		public MultipartFile getMedicalCardFile() {
			return medicalCardFile;
		}
		public void setMedicalCardFile(MultipartFile medicalCardFile) {
			this.medicalCardFile = medicalCardFile;
		}
		public Date getCdlExpireDate() {
			return cdlExpireDate;
		}
		public void setCdlExpireDate(Date cdlExpireDate) {
			this.cdlExpireDate = cdlExpireDate;
		}
		public Date getPhysicalExpireDate() {
			return physicalExpireDate;
		}
		public void setPhysicalExpireDate(Date physicalExpireDate) {
			this.physicalExpireDate = physicalExpireDate;
		}
		public Date getMedicalExpireDate() {
			return medicalExpireDate;
		}
		public void setMedicalExpireDate(Date medicalExpireDate) {
			this.medicalExpireDate = medicalExpireDate;
		}
	}

	@RequestMapping(value = "/secure/driver/mydata/residency_history", method = RequestMethod.GET)
	public String residencyHistory(HttpServletRequest req) {
		
		if (req.getParameter("next") != null) {
			return "redirect:/secure/driver/mydata/driver_information";
		} else {
			// Get residence history if it exists
			Person person = authService.getEffectiveUser();
			if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
				Driver driver = person.getDriver();
				req.setAttribute("noAdditionalAddresses", driver.getNoAdditionalAddresses());
				req.setAttribute("residences", residenceDao.getResidences(driver));
			}
			
			return "residency_history.page";
		}
	}

	@RequestMapping(value = "/secure/driver/mydata/residency_history", method = RequestMethod.POST)
	public String residencyHistoryPost(@ModelAttribute("residenceForm") @Valid ResidenceForm form,
			Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "residence_edit.page";
		}

		// Save the data
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER) {
			Driver driver = person.getDriver();
			if (driver == null && person.getType() == PersonType.DRIVER) {
				driver = createNewDriver(person);
			}

			if (driver != null) {
				driver.setNoAdditionalAddresses(form.getNoAdditionalAddresses());
				driverDao.update(driver);
				
				Residence residence = null;
				boolean isNew = (form.getUuid() == null || form.getUuid().isEmpty());
				if (isNew) {
					residence = new Residence();
					residence.setCreatedDate(new Date());
				} else {
					residence = residenceDao.findByUuid(form.getUuid());
				}
				residence.setDriver(driver);
				residence.setAddress1(form.getAddress1());
				residence.setAddress2(form.getAddress2());
				residence.setCity(form.getCity());
				residence.setState(form.getState());
				residence.setPostalCode(form.getPostalCode());
				if (isNew) {
					residenceDao.save(residence);
				} else {
					residenceDao.update(residence);
				}
			}
		}
		
		// Handle save/next
		if (req.getParameter("next") != null) {
			return "redirect:/secure/driver/mydata/driver_information?message=success";
		} else {
			return "redirect:/secure/driver/mydata/residency_history?message=success";
		}

	}
	
	@RequestMapping(value="/secure/driver/mydata/residences/new", method=RequestMethod.GET)
	public String residenceNew(@ModelAttribute("residenceForm") ResidenceForm form, HttpServletRequest req) {
	  return "residence_edit.page"; 
	}
	
	@RequestMapping(value="/secure/driver/mydata/residences/{uuid}", method=RequestMethod.GET)
	public String residenceEdit(@PathVariable String uuid, @ModelAttribute("residenceForm") ResidenceForm form, HttpServletRequest req) {
		
		req.setAttribute("uuid", uuid);
		
		Residence residence = residenceDao.findByUuid(uuid);
		form.setUuid(residence.getUuid());
		form.setAddress1(residence.getAddress1());
		form.setAddress2(residence.getAddress2());
		form.setCity(residence.getCity());
		form.setState(residence.getState());
		form.setPostalCode(residence.getPostalCode());
		
		return "residence_edit.page"; 
	}

	@RequestMapping(value="/secure/driver/mydata/residences/{uuid}/delete", method=RequestMethod.GET)
	public String residenceDelete(@PathVariable String uuid) {
		
		Residence toDelete = residenceDao.findByUuid(uuid);
		residenceDao.delete(toDelete);
		
		return "redirect:/secure/driver/mydata/residency_history?message=success";
	}

	public static class ResidenceForm {
		private String uuid;
		@NotEmpty
		private String address1;
		private String address2;
		@NotEmpty
		private String city;
		@NotEmpty
		private String state;
		@NotEmpty
		private String postalCode;
		private Boolean noAdditionalAddresses;
		
		public String getUuid() {
			return this.uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public String getAddress1() {
			return address1;
		}
		public void setAddress1(String address1) {
			this.address1 = address1;
		}
		public String getAddress2() {
			return address2;
		}
		public void setAddress2(String address2) {
			this.address2 = address2;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getPostalCode() {
			return postalCode;
		}
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		public Boolean getNoAdditionalAddresses() {
			return noAdditionalAddresses;
		}
		public void setNoAdditionalAddresses(Boolean noAdditionalAddresses) {
			this.noAdditionalAddresses = noAdditionalAddresses;
		}
	}
	
	@RequestMapping(value = "/secure/driver/mydata/driver_information", method = RequestMethod.GET)
	public String driverInformation(@ModelAttribute("driverInformationForm") DriverInformationForm form, HttpServletRequest req) {
		
		// Prefill with driver info if it exists
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
			Driver driver = person.getDriver();
			form.setAvailableDate(driver.getAvailableDate());
			form.setControlledSubstance(driver.getControlledSubstance());
			form.setHighestGradeCompleted(driver.getHighestGradeCompleted());
			form.setDriverSchool(driver.getDriverSchool());
			form.setDriverSchoolName(driver.getDriverSchoolName());
			form.setEligibleEmployment(driver.getEligibleEmployment());
			form.setNotEligibleExplanation(driver.getNotEligibleExplanation());
			form.setDuiConviction(driver.getDuiConviction());
			form.setFelonyConviction(driver.getFelonyConviction());
			form.setLicenseRevoked(driver.getLicenseRevoked());
		}
		
		return "driver_information.page";
	}

	@RequestMapping(value = "/secure/driver/mydata/driver_information", method = RequestMethod.POST)
	public String driverInformationPost(@ModelAttribute("driverInformationForm") @Valid DriverInformationForm form,
			Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "driver_information.page";
		}

		if (form.getAvailableDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(form.getAvailableDate());
			ValidationUtils.rejectInvalidYear(cal.get(Calendar.YEAR), errors, "availableDate", "error.invalid.year", 2010, null);
		}
		if (errors.hasErrors()) {
			return "driver_information.page";
		}

		// Save the data
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER) {
			Driver driver = driverDao.getDriver(person);
			if (driver == null && person.getType() == PersonType.DRIVER) {
				driver = createNewDriver(person);
			}
			
			if (driver != null) {
				driver.setAvailableDate(form.getAvailableDate());
				driver.setFelonyConviction(form.getFelonyConviction());
				driver.setDuiConviction(form.getDuiConviction());
				driver.setLicenseRevoked(form.getLicenseRevoked());
				driver.setControlledSubstance(form.getControlledSubstance());
				driver.setHighestGradeCompleted(form.getHighestGradeCompleted());
				driver.setDriverSchool(form.getDriverSchool());
				driver.setDriverSchoolName(form.getDriverSchoolName());
				driver.setEligibleEmployment(form.getEligibleEmployment());
				driver.setNotEligibleExplanation(form.getNotEligibleExplanation());
				driverDao.update(driver);
			}
		}
		
		// Handle save/next
		if (req.getParameter("next") != null) {
			return "redirect:/secure/driver/mydata/cdl_information?message=success";
		} else {
			return "redirect:/secure/driver/mydata/driver_information?message=success";
		}
	}
	
	public static class DriverInformationForm {
		@NotNull
		private Date availableDate;
		@NotNull
		private Boolean felonyConviction;
		@NotNull
		private Boolean duiConviction;
		@NotNull
		private Boolean licenseRevoked;
		@NotNull
		private Boolean controlledSubstance;
		@NotEmpty
		private String highestGradeCompleted;
		@NotNull
		private Boolean driverSchool;
		private String driverSchoolName;
		@NotNull
		private Boolean eligibleEmployment;
		private String notEligibleExplanation;
		
		public Date getAvailableDate() {
			return availableDate;
		}
		public void setAvailableDate(Date availableDate) {
			this.availableDate = availableDate;
		}
		public Boolean getFelonyConviction() {
			return felonyConviction;
		}
		public void setFelonyConviction(Boolean felonyConviction) {
			this.felonyConviction = felonyConviction;
		}
		public Boolean getDuiConviction() {
			return duiConviction;
		}
		public void setDuiConviction(Boolean duiConviction) {
			this.duiConviction = duiConviction;
		}
		public Boolean getLicenseRevoked() {
			return licenseRevoked;
		}
		public void setLicenseRevoked(Boolean licenseRevoked) {
			this.licenseRevoked = licenseRevoked;
		}
		public Boolean getControlledSubstance() {
			return controlledSubstance;
		}
		public void setControlledSubstance(Boolean controlledSubstance) {
			this.controlledSubstance = controlledSubstance;
		}
		public String getHighestGradeCompleted() {
			return highestGradeCompleted;
		}
		public void setHighestGradeCompleted(String highestGradeCompleted) {
			this.highestGradeCompleted = highestGradeCompleted;
		}
		public Boolean getDriverSchool() {
			return driverSchool;
		}
		public void setDriverSchool(Boolean driverSchool) {
			this.driverSchool = driverSchool;
		}
		public String getDriverSchoolName() {
			return driverSchoolName;
		}
		public void setDriverSchoolName(String driverSchoolName) {
			this.driverSchoolName = driverSchoolName;
		}
		public Boolean getEligibleEmployment() {
			return eligibleEmployment;
		}
		public void setEligibleEmployment(Boolean eligibleEmployment) {
			this.eligibleEmployment = eligibleEmployment;
		}
		public String getNotEligibleExplanation() {
			return notEligibleExplanation;
		}
		public void setNotEligibleExplanation(String notEligibleExplanation) {
			this.notEligibleExplanation = notEligibleExplanation;
		}
	}

	@RequestMapping(value = "/secure/driver/mydata/cdl_information", method = RequestMethod.GET)
	public String cdlInformation(HttpServletRequest req) {

		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
			req.setAttribute("licenses", licenseDao.getLicenses(person.getDriver()));
		}

		return "cdl_information.page";
	}

	@RequestMapping(value = "/secure/driver/mydata/cdl_information", method = RequestMethod.POST)
	public String cdlInformationPost(@ModelAttribute("cdlInformationForm") @Valid CdlInformationForm form,
			Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "cdl_edit.page";
		}
		
		// Save the data
		Person person = authService.getEffectiveUser();
		Driver driver = person.getDriver();
		if (driver == null && person.getType() == PersonType.DRIVER) {
			driver = createNewDriver(person);
		}
		
		if (driver != null) {
			License license = null;
			
			if (form.getUuid() == null || form.getUuid().isEmpty()) {
				license = new License();
				license.setDriver(driver);
				license.setState(form.getState());
				license.setNumber(form.getNumber());
				license.setType(form.getType());
				license.setExpiration(form.getExpiration());
				license.setCreatedDate(new Date());
				license.setCurrent(form.isCurrent());
				licenseDao.save(license);
			} else {
				license = licenseDao.findByUuid(form.getUuid());
				license.setCurrent(form.isCurrent());
				license.setState(form.getState());
				license.setNumber(form.getNumber());
				license.setType(form.getType());
				license.setExpiration(form.getExpiration());
				licenseDao.update(license);
			}
		}
		
		return "redirect:/secure/driver/mydata/cdl_information?message=success";
	}
	
	@RequestMapping(value="/secure/driver/mydata/licenses/new", method=RequestMethod.GET)
	public String licenseNew(@ModelAttribute("cdlInformationForm") CdlInformationForm form, HttpServletRequest req) {
	  return "cdl_edit.page";
	}
	
	@RequestMapping(value="/secure/driver/mydata/licenses/{uuid}", method=RequestMethod.GET)
	public String licenseEdit(@PathVariable String uuid, @ModelAttribute("cdlInformationForm") CdlInformationForm form, HttpServletRequest req) {
		
		req.setAttribute("uuid", uuid);
		
		License license = licenseDao.findByUuid(uuid);
		form.setUuid(license.getUuid());
		form.setState(license.getState());
		form.setNumber(license.getNumber());
		form.setType(license.getType());
		form.setExpiration(license.getExpiration());
		form.setCurrent(license.isCurrent());
		
		return "cdl_edit.page";
	}

	@RequestMapping(value="/secure/driver/mydata/licenses/{uuid}/delete", method=RequestMethod.GET)
	public String licenseDelete(@PathVariable String uuid) {
		
		License toDelete = licenseDao.findByUuid(uuid);
		licenseDao.delete(toDelete);
		
		return "redirect:/secure/driver/mydata/cdl_information?message=success";
	}
	
	public static class CdlInformationForm {
		private String uuid;
		@NotEmpty
		private String state;
		@NotEmpty
		private String number;
		private LicenseType type;
		private Date expiration;
		private boolean current;
		private boolean hadAnother;
		
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public LicenseType getType() {
			return type;
		}
		public void setType(LicenseType type) {
			this.type = type;
		}
		public Date getExpiration() {
			return expiration;
		}
		public void setExpiration(Date expiration) {
			this.expiration = expiration;
		}
		public boolean isCurrent() {
			return current;
		}
		public void setCurrent(boolean current) {
			this.current = current;
		}
		public boolean isHadAnother() {
			return hadAnother;
		}
		public void setHadAnother(boolean another) {
			this.hadAnother = another;
		}
	}
	
	@RequestMapping(value = "/secure/driver/mydata/accident_information", method = RequestMethod.GET)
	public String accidentInformation(HttpServletRequest req) {

		// Get driver accidents
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
			req.setAttribute("accidents", accidentDao.getAccidents(person.getDriver()));
		}

		return "accident_information.page";
	}

	@RequestMapping(value = "/secure/driver/mydata/accident_information", method = RequestMethod.POST)
	public String accidentInformationPost(@ModelAttribute("accidentForm") @Valid AccidentForm form,
			Errors errors, HttpServletRequest req) {
		
		if (errors.hasErrors()) {
			return "accident_edit.page";
		}

		// Save the data
		Person person = authService.getEffectiveUser();
		Driver driver = person.getDriver();
		if (driver == null && person.getType() == PersonType.DRIVER) {
			driver = createNewDriver(person);
		}
		
		if (driver != null) {
 			Accident accident = null;
			
			if (form.getUuid() == null || form.getUuid().isEmpty()) {
				accident = new Accident();
				accident.setDriver(driver);
				accident.setCreatedDate(new Date());
				accident.setAccidentDate(form.getAccidentDate());
				accident.setType(form.getType());
				accident.setNature(form.getNature());
				accident.setAtFault(form.getAtFault());
				accident.setFatalities(form.getFatalities());
				accident.setInjuries(form.getInjuries());
				accident.setDamages(form.getDamages());
				accidentDao.save(accident);
			} else {
				accident = accidentDao.findByUuid(form.getUuid());
				accident.setAccidentDate(form.getAccidentDate());
				accident.setType(form.getType());
				accident.setNature(form.getNature());
				accident.setAtFault(form.getAtFault());
				accident.setFatalities(form.getFatalities());
				accident.setInjuries(form.getInjuries());
				accident.setDamages(form.getDamages());
				accidentDao.update(accident);
			}
		}
		
		return "redirect:/secure/driver/mydata/accident_information?message=success";
	}
	
	@RequestMapping(value="/secure/driver/mydata/accidents/new", method=RequestMethod.GET)
	public String accidentNew(@ModelAttribute("accidentForm") AccidentForm form, HttpServletRequest req) {
	  return "accident_edit.page"; 
	}
	
	@RequestMapping(value="/secure/driver/mydata/accidents/{uuid}", method=RequestMethod.GET)
	public String accidentEdit(@PathVariable String uuid, @ModelAttribute("accidentForm") AccidentForm form, HttpServletRequest req) {
		
		req.setAttribute("uuid", uuid);
		Accident accident = accidentDao.findByUuid(uuid);
		form.setUuid(accident.getUuid());
		form.setAccidentDate(accident.getAccidentDate());
		form.setType(accident.getType());
		form.setNature(accident.getNature());
		form.setAtFault(accident.getAtFault());
		form.setFatalities(accident.getFatalities());
		form.setInjuries(accident.getInjuries());
		form.setDamages(accident.getDamages());
		
		return "accident_edit.page"; 
	}

	@RequestMapping(value="/secure/driver/mydata/accidents/{uuid}/delete", method=RequestMethod.GET)
	public String accidentDelete(@PathVariable String uuid) {
		
		Accident toDelete = accidentDao.findByUuid(uuid);
		accidentDao.delete(toDelete);
		
		return "redirect:/secure/driver/mydata/accident_information?message=success";
	}
	
	public static class AccidentForm {
		private String uuid;
		@NotNull
		private Date accidentDate;
		@NotEmpty
		private String type;
		private String nature;
		@NotNull
		private Boolean atFault;
		@NotNull
		private Boolean fatalities;
		@NotNull
		private Boolean injuries;
		private BigDecimal damages;

		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public Date getAccidentDate() {
			return accidentDate;
		}
		public void setAccidentDate(Date accidentDate) {
			this.accidentDate = accidentDate;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getNature() {
			return nature;
		}
		public void setNature(String nature) {
			this.nature = nature;
		}
		public Boolean getAtFault() {
			return atFault;
		}
		public void setAtFault(Boolean atFault) {
			this.atFault = atFault;
		}
		public Boolean getFatalities() {
			return fatalities;
		}
		public void setFatalities(Boolean fatalities) {
			this.fatalities = fatalities;
		}
		public Boolean getInjuries() {
			return injuries;
		}
		public void setInjuries(Boolean injuries) {
			this.injuries = injuries;
		}
		public BigDecimal getDamages() {
			return damages;
		}
		public void setDamages(BigDecimal damages) {
			this.damages = damages;
		}
	}

	@RequestMapping(value = "/secure/driver/mydata/employment_history", method = RequestMethod.GET)
	public String employmentHistory(HttpServletRequest req) {
		
		// Get employment history if it exists
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
			req.setAttribute("employmentHistory", employmentDao.getEmployments(person.getDriver()));
		}
		
		return "employment_history.page";
	}
	
	@RequestMapping(value="/secure/driver/mydata/employments/new", method=RequestMethod.GET)
	public String employmentNew(@ModelAttribute("employmentForm") EmploymentForm form, HttpServletRequest req) {
	  return "employment_edit.page"; 
	}
	
	@RequestMapping(value="/secure/driver/mydata/employments/{uuid}", method=RequestMethod.GET)
	public String employmentEdit(@PathVariable String uuid, @ModelAttribute("employmentForm") EmploymentForm form, HttpServletRequest req) {
		
		Employment employ = employmentDao.findByUuid(uuid);
		form.setUuid(employ.getUuid());
		form.setName(employ.getName());
		form.setSupervisor(employ.getSupervisor());
		form.setAddress(employ.getAddress());
		form.setCity(employ.getCity());
		form.setState(employ.getState());
		form.setPostalCode(employ.getPostalCode());
		form.setPhone(employ.getPhone());
		form.setPosition(employ.getPosition());
		form.setFromDate(employ.getFromDate());
		form.setToDate(employ.getToDate());
		form.setLeaving(employ.getLeaving());
		
		return "employment_edit.page"; 
	}

	@RequestMapping(value="/secure/driver/mydata/employments/{uuid}/delete", method=RequestMethod.GET)
	public String employmentDelete(@PathVariable String uuid) {
		
		Employment toDelete = employmentDao.findByUuid(uuid);
		employmentDao.delete(toDelete);
		
		return "redirect:/secure/driver/mydata/employment_history?message=success";
	}

	@RequestMapping(value = "/secure/driver/mydata/employment_history", method = RequestMethod.POST)
	public String employmentHistoryPost(@ModelAttribute("employmentForm") @Valid EmploymentForm form,
			Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "employment_edit.page";
		}
		
		// Save the data
		Person person = authService.getEffectiveUser();
		Driver driver = person.getDriver();
		if (driver == null && person.getType() == PersonType.DRIVER) {
			driver = createNewDriver(person);
		}
		boolean isNew = (form.getUuid() == null || form.getUuid().isEmpty());
		
		if (driver != null) {
			Employment employ = null;
			
			if (isNew) {
				employ = new Employment();
			} else {
				employ = employmentDao.findByUuid(form.getUuid());
			}
			employ.setDriver(driver);
			employ.setName(form.getName());
			employ.setSupervisor(form.getSupervisor());
			employ.setAddress(form.getAddress());
			employ.setCity(form.getCity());
			employ.setState(form.getState());
			employ.setPostalCode(form.getPostalCode());
			employ.setPhone(form.getPhone());
			employ.setPosition(form.getPosition());
			employ.setFromDate(form.getFromDate());
			employ.setToDate(form.getToDate());
			employ.setLeaving(form.getLeaving());
			if (isNew) {
				employmentDao.save(employ);
			} else {
				employmentDao.update(employ);
			}
		}
		
		return "redirect:/secure/driver/mydata/employment_history?message=success";
	}
	
	public static class EmploymentForm {
		private String uuid;
		@NotEmpty
		private String name;
		private String supervisor;
		@NotEmpty
		private String address;
		@NotEmpty
		private String city;
		@NotEmpty
		private String state;
		@NotEmpty
		private String postalCode;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String phone;
		private String position;
		private Date fromDate;
		private Date toDate;
		private String leaving;
		
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSupervisor() {
			return supervisor;
		}
		public void setSupervisor(String supervisor) {
			this.supervisor = supervisor;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getPostalCode() {
			return postalCode;
		}
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public Date getFromDate() {
			return fromDate;
		}
		public void setFromDate(Date fromDate) {
			this.fromDate = fromDate;
		}
		public Date getToDate() {
			return toDate;
		}
		public void setToDate(Date toDate) {
			this.toDate = toDate;
		}
		public String getLeaving() {
			return leaving;
		}
		public void setLeaving(String leaving) {
			this.leaving = leaving;
		}
	}

	@RequestMapping(value = "/secure/driver/mydata/traffic_convictions", method = RequestMethod.GET)
	public String trafficConvictions(HttpServletRequest req) {
		
		// Get traffic convictions
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.DRIVER && person.getDriver() != null) {
			req.setAttribute("trafficRecords", trafficDao.getTrafficRecords(person.getDriver()));
		}
		
		return "traffic_convictions.page";
	}

	@RequestMapping(value = "/secure/driver/mydata/traffic_convictions", method = RequestMethod.POST)
	public String trafficConvictionsPost(@ModelAttribute("trafficForm") @Valid TrafficForm form,
			Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "traffic_edit.page";
		}
		
		// Save the data
		Person person = authService.getEffectiveUser();
		Driver driver = person.getDriver();
		if (driver == null && person.getType() == PersonType.DRIVER) {
			driver = createNewDriver(person);
		}
		boolean isNew = (form.getUuid() == null || form.getUuid().isEmpty());
		
		if (driver != null) {
			Traffic traffic = null;
			
			if (isNew) {
				traffic = new Traffic();
				traffic.setCreatedDate(new Date());
			} else {
				traffic = trafficDao.findByUuid(form.getUuid());
			}
			traffic.setDriver(driver);
			traffic.setTrafficDate(form.getTrafficDate());
			traffic.setCity(form.getCity());
			traffic.setState(form.getState());
			traffic.setCharge(form.getCharge());
			traffic.setPenalty(form.getPenalty());
			if (isNew) {
				trafficDao.save(traffic);
			} else {
				trafficDao.update(traffic);
			}
		}
		
		return "redirect:/secure/driver/mydata/traffic_convictions?message=success";
	}
	
	@RequestMapping(value="/secure/driver/mydata/traffics/new", method=RequestMethod.GET)
	public String trafficNew(@ModelAttribute("trafficForm") TrafficForm form, HttpServletRequest req) {
	  return "traffic_edit.page"; 
	}
	
	@RequestMapping(value="/secure/driver/mydata/traffics/{uuid}", method=RequestMethod.GET)
	public String trafficEdit(@PathVariable String uuid, @ModelAttribute("trafficForm") TrafficForm form, HttpServletRequest req) {
		
		Traffic traffic = trafficDao.findByUuid(uuid);
		form.setUuid(traffic.getUuid());
		form.setTrafficDate(traffic.getTrafficDate());
		form.setCity(traffic.getCity());
		form.setState(traffic.getState());
		form.setCharge(traffic.getCharge());
		form.setPenalty(traffic.getPenalty());
		
		return "traffic_edit.page"; 
	}

	@RequestMapping(value="/secure/driver/mydata/traffics/{uuid}/delete", method=RequestMethod.GET)
	public String trafficDelete(@PathVariable String uuid) {
		
		Traffic toDelete = trafficDao.findByUuid(uuid);
		trafficDao.delete(toDelete);
		
		return "redirect:/secure/driver/mydata/traffic_convictions?message=success";
	}

	public static class TrafficForm {
		private String uuid;
		@NotNull
		private Date trafficDate;
		@NotEmpty
		private String city;
		@NotEmpty
		private String state;
		@NotEmpty
		private String charge;
		@NotEmpty
		private String penalty;
		
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public Date getTrafficDate() {
			return trafficDate;
		}
		public void setTrafficDate(Date trafficDate) {
			this.trafficDate = trafficDate;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getCharge() {
			return charge;
		}
		public void setCharge(String charge) {
			this.charge = charge;
		}
		public String getPenalty() {
			return penalty;
		}
		public void setPenalty(String penalty) {
			this.penalty = penalty;
		}
	}
	
	private Driver createNewDriver(Person person) {
		Driver driver = new Driver();
		driver.setPerson(person);
		driver.setCreatedBy(person);
		driver.setCreatedDate(new Date());
		driverDao.save(driver);

		return driver;
	}

}
