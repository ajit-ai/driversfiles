package com.driversfiles.www.common;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.core.dao.CompanyTruckDao;
import com.driversfiles.www.core.dao.DriverDao;
import com.driversfiles.www.core.dao.DriverTruckDao;
import com.driversfiles.www.core.dao.TruckDao;
import com.driversfiles.www.core.data.CompanyTruck;
import com.driversfiles.www.core.data.Driver;
import com.driversfiles.www.core.data.DriverTruck;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import com.driversfiles.www.core.data.State;
import com.driversfiles.www.core.data.Truck;
import com.driversfiles.www.fs.FileStoreService;
import com.driversfiles.www.fs.ImageInfo;
import com.driversfiles.www.fs.PdfUtil;

import java.util.Arrays;

/**
 * Handles truck requests.
 *
 * @author Erik R. Jensen
 * @author Mark Burns
 */
@Controller
public class TrucksController {

	private static final Logger log = LoggerFactory.getLogger(TrucksController.class);
	private static final String[] ACCEPTABLE_DOC_MIME_TYPES = {"application/pdf", "image/jpeg", "image/gif", "image/png", "image/tif"};
	private static final String[] DOC_EXTENSIONS = {"pdf", "jpg", "gif", "png", "tif"};
	private static final String[] VALID_FORMAT_NAMES = {"JPEG", "GIF", "PNG", "IFF", "TIFF"};
	
	@Autowired
	@Qualifier("authService")
	private AuthService authService;

	@Autowired
	@Qualifier("companyTruckDao")
	private CompanyTruckDao companyTruckDao;
	
	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	@Autowired
	@Qualifier("driverTruckDao")
	private DriverTruckDao driverTruckDao;
	
	@Autowired
	@Qualifier("fileStoreService")
	private FileStoreService fileStoreService;

	@Autowired
	@Qualifier("truckDao")
	private TruckDao truckDao;

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

	@ModelAttribute("acceptTypes")
	String[] getAcceptableDocTypes() {
		return ACCEPTABLE_DOC_MIME_TYPES;
	}

	@RequestMapping(value = "/secure/common/trucks", method = RequestMethod.GET)
	public String trucks(@ModelAttribute("truckSearchForm") TruckSearchForm form, HttpServletRequest req) {
		
		Person person = authService.getEffectiveUser();
		req.setAttribute("trucks", truckDao.getTrucksFilteredByEffectiveUser(null, null, null, null, null));
		
		return person.getType().name().toLowerCase() + "_trucks.page";
	}
	
	@RequestMapping(value = "/secure/common/trucks", method = RequestMethod.POST)
	public String trucksSearch(@ModelAttribute("truckSearchForm") TruckSearchForm form, HttpServletRequest req) {
		if (req.getParameter("clear") != null) {
			form.clear();
		}
		req.setAttribute("trucks", truckDao.getTrucksFilteredByEffectiveUser(
				form.getVin(), form.getYear(), form.getMake(), form.getModel(), form.getActive()));
		Person p = authService.getEffectiveUser();
		return p.getType().name().toLowerCase() + "_trucks.page";
	}
	
	@RequestMapping(value = "/secure/common/trucks/new", method = RequestMethod.GET)
	public String newTruck(@ModelAttribute("truckForm") TruckForm form, HttpServletRequest req) {
		
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.COMPANY)
			req.setAttribute("companyType", true);
		
		return person.getType().name().toLowerCase() + "_truck.page";
	}
	
	@RequestMapping(value = "/secure/common/trucks/{truckUuid}", method = RequestMethod.GET)
	public String editTruck(@PathVariable String truckUuid, @ModelAttribute("truckForm") TruckForm form, HttpServletRequest req) {
		
		Truck truck = truckDao.findByUuid(truckUuid);
		
		form.setUuid(truckUuid);
		form.setVin(truck.getVin());
		form.setYear(truck.getYear());
		form.setMake(truck.getMake());
		form.setModel(truck.getModel());
		form.setLicense(truck.getLicense());
		form.setLicenseState(truck.getLicenseState());
		form.setRegistration(truck.getRegistration());
		form.setAnnualInspection(truck.getAnnualInspection());
		form.setBobtailInsurance(truck.getBobtailInsurance());
		form.setIfta(truck.getIfta());
		form.setQuarterlyMaintenance(truck.getQuarterlyMaintenance());
		form.setPhysicalDamageInsurance(truck.getPhysicalDamageInsurance());
		form.setLessorNumber(truck.getLessorNumber());
		form.setLessorName(truck.getLessorName());
		form.setLessorAddress1(truck.getLessorAddress1());
		form.setLessorAddress2(truck.getLessorAddress2());
		form.setLessorCity(truck.getLessorCity());
		form.setLessorState(truck.getLessorState());
		form.setLessorPostalCode(truck.getLessorPostalCode());
		form.setLessorPhone(truck.getLessorPhone());
		form.setLessorMobile(truck.getLessorMobile());
		form.setLessorGovId(truck.getLessorGovId());
		form.setActive(truck.isActive());
		
		List<File> files = fileStoreService.listFiles(String.format("trucks/%s/documents/", truckUuid));
		List<TruckDocument> docs = new ArrayList<TruckDocument>(files.size());
		for (File file : files) {
			String name = file.getName();
			String typeName = removeExtension(name);
			TruckDocumentType type = TruckDocumentType.findByName(typeName);
			String url = String.format("/secure/api/trucks/%s/documents/%s", truckUuid, name);
			docs.add(new TruckDocument(url, type, getMimeType(file)));
		}
		req.setAttribute("docs", docs);
		
		Person person = authService.getEffectiveUser();
		if (person.getType() == PersonType.ADMIN) {
			// No extra fields
		} else if (person.getType() == PersonType.COMPANY) {
			CompanyTruck coTruck = companyTruckDao.get(person.getCompany(), truck);
			form.setTruckNumber(coTruck.getTruckNumber());
			req.setAttribute("companyType", true);
		} else if (person.getType() == PersonType.DRIVER) {
			// DriverTruck has no extra fields so nothing to do
		}
		return person.getType().name().toLowerCase() + "_truck.page";
	}
	
	@RequestMapping(value = "/secure/common/trucks/{truckUuid}/delete", method = RequestMethod.GET)
	public String deleteTruck(@PathVariable String truckUuid, HttpServletRequest req) {
		
		Truck toDelete = truckDao.findByUuid(truckUuid);
		truckDao.delete(toDelete);
		
		return "redirect:/secure/common/trucks?message=success";
	}
	
	@RequestMapping(value = "/secure/common/trucks/{truckUuid}/deletedoc/{name}", method = RequestMethod.GET)
	public String deleteTruckDocument(@PathVariable String truckUuid, @PathVariable String name, HttpServletRequest req) {
		
		String filename = String.format("trucks/%s/documents/%s", truckUuid, name);
		fileStoreService.delete(filename);
		
		return "redirect:/secure/common/trucks/" + truckUuid;
	}
	
	@RequestMapping(value = "/secure/common/trucks/save", method = RequestMethod.POST)
	public String saveTruck(@ModelAttribute("truckForm") @Valid TruckForm form, Errors errors, HttpServletRequest req) {
		
		if (req.getParameter("cancel") != null) {
			return "redirect:/secure/common/trucks";
		}
		
		Person person = authService.getEffectiveUser();
		String page = person.getType().name().toLowerCase() + "_truck.page";
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return page;
		}
		
		// Save the data
		Date now = new Date();
		boolean isNew = (form.getUuid() == null || form.getUuid().isEmpty());
		Truck truck = null;
			
		if (isNew) {
			truck = new Truck();
			truck.setCreatedBy(person);
			truck.setCreatedDate(now);
			truck.setLastModifiedBy(person);
			truck.setLastModifiedDate(now);
		} else {
			truck = truckDao.findByUuid(form.getUuid());
			truck.setLastModifiedBy(person);
			truck.setLastModifiedDate(now);
		}

		truck.setVin(form.getVin());
		truck.setYear(form.getYear());
		truck.setMake(form.getMake());
		truck.setModel(form.getModel());
		truck.setLicense(form.getLicense());
		truck.setLicenseState(form.getLicenseState());
		truck.setRegistration(form.getRegistration());
		truck.setAnnualInspection(form.getAnnualInspection());
		truck.setBobtailInsurance(form.getBobtailInsurance());
		truck.setIfta(form.getIfta());
		truck.setQuarterlyMaintenance(form.getQuarterlyMaintenance());
		truck.setPhysicalDamageInsurance(form.getPhysicalDamageInsurance());
		truck.setLessorNumber(form.getLessorNumber());
		truck.setLessorName(form.getLessorName());
		truck.setLessorAddress1(form.getLessorAddress1());
		truck.setLessorAddress2(form.getLessorAddress2());
		truck.setLessorCity(form.getLessorCity());
		truck.setLessorState(form.getLessorState());
		truck.setLessorPostalCode(form.getLessorPostalCode());
		truck.setLessorPhone(form.getLessorPhone());
		truck.setLessorMobile(form.getLessorMobile());
		truck.setLessorGovId(form.getLessorGovId());
		truck.setActive(form.isActive());

		if (isNew) {
			truckDao.save(truck);
			
			// Create additional object depending on who it is for
			if (person.getType() == PersonType.ADMIN) {
				// No additional work
			} else if (person.getType() == PersonType.COMPANY) {
				CompanyTruck coTruck = new CompanyTruck();
				coTruck.setCompany(person.getCompany());
				coTruck.setTruck(truck);
				coTruck.setCreatedBy(person);
				coTruck.setCreatedDate(now);
				coTruck.setLastModifiedBy(person);
				coTruck.setLastModifiedDate(now);
				coTruck.setTruckNumber(form.getTruckNumber());
				companyTruckDao.save(coTruck);
				
			} else if (person.getType() == PersonType.DRIVER) {
				Driver driver = driverDao.getDriver(person);
				DriverTruck drTruck = new DriverTruck();
				drTruck.setDriver(driver);
				drTruck.setTruck(truck);
				drTruck.setCreatedBy(person);
				drTruck.setCreatedDate(now);
				drTruck.setLastModifiedBy(person);
				drTruck.setLastModifiedDate(now);
				driverTruckDao.save(drTruck);
				
			}
		} else {
			truckDao.update(truck);
			
			if (person.getType() == PersonType.COMPANY) {
				CompanyTruck coTruck = companyTruckDao.get(person.getCompany(), truck);
				if (!form.getTruckNumber().equals(coTruck.getTruckNumber())) {
					coTruck.setLastModifiedBy(person);
					coTruck.setLastModifiedDate(now);
					coTruck.setTruckNumber(form.getTruckNumber());
					companyTruckDao.update(coTruck);
				}
			}
		}
		
		// Handle each possible doc
		String truckUuid = truck.getUuid();
		String which = null;
		String ext = null;
		try {
			MultipartFile registrationFile = form.getRegistrationFile();
			if (registrationFile != null && registrationFile.getSize() > 0) {
				if ((ext = getDocFileType(registrationFile)) != null) {
					which = "registrationFile";
					fileStoreService.saveFile(getFilename(
							TruckDocumentType.DOC_TYPE_REG, truckUuid, ext), 
							registrationFile.getInputStream());
				} else {
					errors.rejectValue("registrationFile", "truck.doc.invalid");
				}
			}
			
			MultipartFile annualInspFile = form.getAnnualInspFile();
			if (annualInspFile != null && annualInspFile.getSize() > 0) {
				if ((ext = getDocFileType(annualInspFile)) != null) {
					which = "annualInspFile";
					fileStoreService.saveFile(getFilename(
							TruckDocumentType.DOC_TYPE_ANNUAL, truckUuid, ext), 
							annualInspFile.getInputStream());
				} else {
					errors.rejectValue("annualInspFile", "truck.doc.invalid");
				}
			}
			
			MultipartFile bobtailFile = form.getBobtailFile();
			if (bobtailFile != null && bobtailFile.getSize() > 0) {
				if ((ext = getDocFileType(bobtailFile)) != null) {
					which = "bobtailFile";
					fileStoreService.saveFile(getFilename(
							TruckDocumentType.DOC_TYPE_BOBTAIL, truckUuid, ext), 
							bobtailFile.getInputStream());
				} else {
					errors.rejectValue("bobtailFile", "truck.doc.invalid");
				}
			}
			
			MultipartFile quarterlyMaintFile = form.getQuarterlyMaintFile();
			if (quarterlyMaintFile != null && quarterlyMaintFile.getSize() > 0) {
				if ((ext = getDocFileType(quarterlyMaintFile)) != null) {
					which = "quarterlyMaintFile";
					fileStoreService.saveFile(getFilename(
							TruckDocumentType.DOC_TYPE_QUARTERLY_MAINT, truckUuid, ext), 
							quarterlyMaintFile.getInputStream());
				} else {
					errors.rejectValue("quarterlyMaintFile", "truck.doc.invalid");
				}
			}
			
			MultipartFile physicalDamageInsFile = form.getPhysicalDamageInsFile();
			if (physicalDamageInsFile != null && physicalDamageInsFile.getSize() > 0) {
				if ((ext = getDocFileType(physicalDamageInsFile)) != null) {
					which = "physicalDamageInsFile";
					fileStoreService.saveFile(getFilename(
							TruckDocumentType.DOC_TYPE_PHYSICAL_DAMAGE, truckUuid, ext), 
							physicalDamageInsFile.getInputStream());
				} else {
					errors.rejectValue("physicalDamageInsFile", "truck.doc.invalid");
				}
			}
			
			if (errors.hasErrors()) {
				return page;
			}
			
		} catch (IOException e) {
			log.error("Error in TrucksController.truckDocsPost", e);
			errors.reject(which, "There was a severe error while uploading the file(s)!");
			return page;
		}
		
		return "redirect:/secure/common/trucks?message=success";
	}
	
	public static class TruckForm {

		private String uuid;
		@NotEmpty
		private String vin;
		@NotNull
		private Integer year;
		@NotEmpty
		private String make;
		@NotEmpty
		private String model;
		private String license;
		private String licenseState;
		private Date registration;
		private Date annualInspection;
		private Date bobtailInsurance;
		private Boolean ifta;
		private Date quarterlyMaintenance;
		private Date physicalDamageInsurance;
		private String lessorNumber;
		private String lessorName;
		private String lessorAddress1;
		private String lessorAddress2;
		private String lessorCity;
		private String lessorState;
		private String lessorPostalCode;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String lessorPhone;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String lessorMobile;
		private String lessorGovId;
		private boolean active = true;
		private MultipartFile registrationFile;
		private MultipartFile annualInspFile;
		private MultipartFile bobtailFile;
		private MultipartFile quarterlyMaintFile;
		private MultipartFile physicalDamageInsFile;
		
		// CompanyTruck fields
		private String truckNumber;

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getVin() {
			return vin;
		}

		public void setVin(String vin) {
			this.vin = vin;
		}

		public Integer getYear() {
			return year;
		}

		public void setYear(Integer year) {
			this.year = year;
		}

		public String getMake() {
			return make;
		}

		public void setMake(String make) {
			this.make = make;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

		public String getLicenseState() {
			return licenseState;
		}

		public void setLicenseState(String licenseState) {
			this.licenseState = licenseState;
		}

		public Date getRegistration() {
			return registration;
		}

		public void setRegistration(Date registration) {
			this.registration = registration;
		}

		public Date getAnnualInspection() {
			return annualInspection;
		}

		public void setAnnualInspection(Date annualInspection) {
			this.annualInspection = annualInspection;
		}

		public Date getBobtailInsurance() {
			return bobtailInsurance;
		}

		public void setBobtailInsurance(Date bobtailInsurance) {
			this.bobtailInsurance = bobtailInsurance;
		}

		public Boolean getIfta() {
			return ifta;
		}

		public void setIfta(Boolean ifta) {
			this.ifta = ifta;
		}

		public Date getQuarterlyMaintenance() {
			return quarterlyMaintenance;
		}

		public void setQuarterlyMaintenance(Date quarterlyMaintenance) {
			this.quarterlyMaintenance = quarterlyMaintenance;
		}

		public Date getPhysicalDamageInsurance() {
			return physicalDamageInsurance;
		}

		public void setPhysicalDamageInsurance(Date physicalDamageInsurance) {
			this.physicalDamageInsurance = physicalDamageInsurance;
		}

		public String getLessorNumber() {
			return lessorNumber;
		}

		public void setLessorNumber(String lessorNumber) {
			this.lessorNumber = lessorNumber;
		}

		public String getLessorName() {
			return lessorName;
		}

		public void setLessorName(String lessorName) {
			this.lessorName = lessorName;
		}

		public String getLessorAddress1() {
			return lessorAddress1;
		}

		public void setLessorAddress1(String lessorAddress1) {
			this.lessorAddress1 = lessorAddress1;
		}

		public String getLessorAddress2() {
			return lessorAddress2;
		}

		public void setLessorAddress2(String lessorAddress2) {
			this.lessorAddress2 = lessorAddress2;
		}

		public String getLessorCity() {
			return lessorCity;
		}

		public void setLessorCity(String lessorCity) {
			this.lessorCity = lessorCity;
		}

		public String getLessorState() {
			return lessorState;
		}

		public void setLessorState(String lessorState) {
			this.lessorState = lessorState;
		}

		public String getLessorPostalCode() {
			return lessorPostalCode;
		}

		public void setLessorPostalCode(String lessorPostalCode) {
			this.lessorPostalCode = lessorPostalCode;
		}

		public String getLessorPhone() {
			return lessorPhone;
		}

		public void setLessorPhone(String lessorPhone) {
			this.lessorPhone = lessorPhone;
		}

		public String getLessorMobile() {
			return lessorMobile;
		}

		public void setLessorMobile(String lessorMobile) {
			this.lessorMobile = lessorMobile;
		}

		public String getLessorGovId() {
			return lessorGovId;
		}

		public void setLessorGovId(String lessorGovId) {
			this.lessorGovId = lessorGovId;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public String getTruckNumber() {
			return truckNumber;
		}

		public void setTruckNumber(String truckNumber) {
			this.truckNumber = truckNumber;
		}

		public MultipartFile getRegistrationFile() {
			return registrationFile;
		}

		public void setRegistrationFile(MultipartFile registrationFile) {
			this.registrationFile = registrationFile;
		}

		public MultipartFile getAnnualInspFile() {
			return annualInspFile;
		}

		public void setAnnualInspFile(MultipartFile annualInspFile) {
			this.annualInspFile = annualInspFile;
		}

		public MultipartFile getBobtailFile() {
			return bobtailFile;
		}

		public void setBobtailFile(MultipartFile bobtailFile) {
			this.bobtailFile = bobtailFile;
		}

		public MultipartFile getQuarterlyMaintFile() {
			return quarterlyMaintFile;
		}

		public void setQuarterlyMaintFile(MultipartFile quarterlyMaintFile) {
			this.quarterlyMaintFile = quarterlyMaintFile;
		}

		public MultipartFile getPhysicalDamageInsFile() {
			return physicalDamageInsFile;
		}

		public void setPhysicalDamageInsFile(MultipartFile physicalDamageInsFile) {
			this.physicalDamageInsFile = physicalDamageInsFile;
		}
	}
	
	public static class TruckSearchForm {

		private String vin;
		private Integer year;
		private String make;
		private String model;
		private Boolean active = null;

		public void clear() {
			vin = null;
			year = null;
			make = null;
			model = null;
			active = null;
		}

		public String getVin() {
			return vin;
		}

		public void setVin(String vin) {
			this.vin = vin;
		}

		public Integer getYear() {
			return year;
		}

		public void setYear(Integer year) {
			this.year = year;
		}

		public String getMake() {
			return make;
		}

		public void setMake(String make) {
			this.make = make;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}
	}

	private String getFilename(TruckDocumentType type, String uuid, String ext) {
		
		return String.format("trucks/%s/documents/%s.%s", uuid, type.getName(), ext);
	}
	
	private String getDocFileType(MultipartFile docFile) {
		
		if (docFile == null || docFile.getSize() > 5242880L) {
			return null;
		}
		
		try {
			// Check if a PDF
			PdfUtil util = new PdfUtil();
			util.setInput(docFile.getInputStream());
			if (util.check()) {
				return DOC_EXTENSIONS[0];
			}
			
			// Check if a valid image file
			ImageInfo info = new ImageInfo();
			info.setInput(docFile.getInputStream());
			
			if (info.check()) {
				@SuppressWarnings("unchecked")
				List<String> validTypes = Arrays.asList(VALID_FORMAT_NAMES);
				String type = info.getFormatName();
				if (type != null && validTypes.contains(type)) {
					return getExtension(type);
				}
			}
			
		} catch (IOException e) {
			log.error("Error in TrucksController.getDocFileType", e);
		}
		
		return null;
	}
	
	private String getExtension(String type) {
		
		if (type.equals("JPEG")) {
			return "jpg";
		}
		if (type.equals("IFF")) {
			return "tif";
		}
		return type.toLowerCase();
	}
	
	private String removeExtension(String filename) {
		
		int index = filename.lastIndexOf('.');
		if (index > 0) {
			return filename.substring(0, index);
		}
		return filename;
	}
	
	private String getMimeType(File file) {
		
		String filename = file.getName();
		int index = filename.lastIndexOf('.');
		if (index > 0) {
			String ext = filename.substring(index+1);
			
			for (int i = 0; i < DOC_EXTENSIONS.length; i++) {
				String docExt = DOC_EXTENSIONS[i];
				if (docExt.equals(ext)) {
					return ACCEPTABLE_DOC_MIME_TYPES[i];
				}
			}
		}
		return "";
	}
	
	public enum TruckDocumentType {
		
		DOC_TYPE_REG("Registration"),
		DOC_TYPE_ANNUAL("Annual Inspection"),
		DOC_TYPE_BOBTAIL("Bobtail"),
		DOC_TYPE_QUARTERLY_MAINT("Quarterly Maintenance"),
		DOC_TYPE_PHYSICAL_DAMAGE("Physical Damage Insurance");
		
		private String title;
		
		private TruckDocumentType(String title) {
			this.title = title;
		}
		
		public static TruckDocumentType findByName(String name) {
			
			for (TruckDocumentType value : values()) {
				if (value.name().equalsIgnoreCase(name)) {
					return value;
				}
			}
			
			return null;
		}
		
		public String getName() {
			return this.name();
		}
		
		public String getTitle() {
			return this.title;
		}

	}

	public static class TruckDocument implements Serializable {
		
		private static final long serialVersionUID = 2801382705120054011L;
		private String url;
		private TruckDocumentType type;
		private String mimetype;
		
		public TruckDocument() {
		}
		
		public TruckDocument(String url, TruckDocumentType type, String mimetype) {
			super();
			this.url = url;
			this.type = type;
			this.mimetype = mimetype;
		}

		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public TruckDocumentType getType() {
			return type;
		}
		public void setType(TruckDocumentType type) {
			this.type = type;
		}
		public String getMimetype() {
			return mimetype;
		}
		public void setMimetype(String mimetype) {
			this.mimetype = mimetype;
		}
	}
}
