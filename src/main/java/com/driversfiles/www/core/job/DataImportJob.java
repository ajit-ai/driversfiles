package com.driversfiles.www.core.job;

import au.com.bytecode.opencsv.CSVReader;
import com.driversfiles.www.core.dao.*;
import com.driversfiles.www.core.data.*;
import com.driversfiles.www.core.service.ImportType;
import com.driversfiles.www.quartz.SpringQuartzJob;
import com.driversfiles.www.util.ThrowableHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles data import.
 *
 * @author Ajit Kumar
 */
public class DataImportJob extends SpringQuartzJob {

	private static final Logger log = LoggerFactory.getLogger(DataImportJob.class);

	private static final int DRIVER_FIELDS = 41;
	private static final int TRUCK_FIELDS = 24;

	@Autowired
	@Qualifier("dataImportDao")
	private DataImportDao dataImportDao;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	@Qualifier("truckDao")
	private TruckDao truckDao;

	@Autowired
	@Qualifier("companyTruckDao")
	private CompanyTruckDao companyTruckDao;

	@Autowired
	@Qualifier("driverDao")
	private DriverDao driverDao;

	@Autowired
	@Qualifier("companyDriverDao")
	private CompanyDriverDao companyDriverDao;

	@Autowired
	@Qualifier("driverTruckDao")
	private DriverTruckDao driverTruckDao;

	@Autowired
	@Qualifier("personDao")
	private PersonDao personDao;

	@Autowired
	@Qualifier("licenseDao")
	private LicenseDao licenseDao;

	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	private DataImport di;
	private StringBuilder sb;
	private Set<String> importedTrucks = new HashSet<String>();

	private void update(Date startTime) {
		if (di != null && startTime != null) {
			dataImportDao.update(di.getId(), startTime);
		}
	}

	private void update(String log) {
		if (di != null && log != null) {
			sb.append(log).append("\n");
			dataImportDao.update(di.getId(), sb.toString());
		}
	}

	private void update(Date endTime, Boolean success) {
		if (di != null && success != null) {
			dataImportDao.update(di.getId(), endTime, success);
		}
	}

	private void update(int line, int field, String msg) {
		if (di != null && msg != null) {
			sb.append("Line " + line + " Field " + field + ": " + msg).append("\n");
			dataImportDao.update(di.getId(), sb.toString());
		}
	}

	private boolean importDriver(int lineNum, DataImport di, String[] line) {
		if (line.length != DRIVER_FIELDS) {
			update("Invalid number of fields on line " + lineNum + ". Expected " + DRIVER_FIELDS + " fields, but found " + line.length);
			return false;
		}
		Driver driver = null;
		CompanyDriver cd;
		Person person;

		int i = 0;

		String tmp = line[i++].trim(); // Driver Number
		if (tmp.isEmpty()) {
			update(lineNum, i, "Driver number is required");
			return false;
		}
		if (tmp.length() > 100 ) {
			update(lineNum, i, "Driver number must be 100 characters or less");
			return false;
		}
		tmp = tmp.toUpperCase();
		cd = companyDriverDao.get(di.getCompany(), tmp);
		if (cd != null) {
			driver = cd.getDriver();
			assert driver != null;
		}
		if (cd != null && !di.isOvewrite()) {
			update(lineNum, i, "Driver with number " + tmp + " already exists");
			return false;
		}
		if (cd == null) {
			driver = new Driver();
			cd = new CompanyDriver(di.getCompany(), driver);
			cd.setDriverNumber(tmp);
		}

		tmp = line[i++].trim(); // Email Address
		if (tmp.isEmpty()) {
			update(lineNum, i, "Email address is required");
			return false;
		}
		if (tmp.length() > 100) {
			update(lineNum, i, "Email address must be 100 characters or less");
			return false;
		}
		person = personDao.findByEmail(tmp);
		if (person != null && !di.isOvewrite()) {
			update(lineNum, i, "Person with email address " + tmp + " already exists");
			return false;
		}
		if (person != null) {
			if (!person.equals(driver.getPerson())) {
				update(lineNum, i, "Another user with the email address " + tmp + " already exits.");
			}
		} else {
			person = new Person();
		}
		person.setType(PersonType.DRIVER);
		person.setEmail(tmp);

		tmp = line[i++].trim(); // Password
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "Password must be 50 characters or less");
				return false;
			}
			person.setPassword(tmp);
		}

		tmp = line[i++].trim(); // First Name
		if (tmp.isEmpty()) {
			update(lineNum, i, "First name is required");
			return false;
		}
		if (tmp.length() > 50) {
			update(lineNum, i, "First name must be 50 characters or less");
			return false;
		}
		person.setFirstName(tmp);

		tmp = line[i++].trim(); // Middle Name
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "Middle name must be 50 characters or less");
				return false;
			}
			person.setMiddleName(tmp);
		}

		tmp = line[i++].trim(); // Last Name
		if (tmp.isEmpty()) {
			update(lineNum, i, "Last name is required");
			return false;
		}
		if (tmp.length() > 50) {
			update(lineNum, i, "Last name must be 50 characters or less");
			return false;
		}
		person.setLastName(tmp);

		tmp = line[i++].trim(); // Date of Birth
		if (!tmp.isEmpty()) {
			try {
				driver.setDob(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid date of birth: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Social Security Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 11) {
				update(lineNum, i, "Social security number must be 11 characters or less");
				return false;
			}
			driver.setSsn(tmp);
		}

		tmp = line[i++].trim(); // Phone Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Phone number must be 20 characters or less");
				return false;
			}
			driver.setPhone(tmp);
		}

		tmp = line[i++].trim(); // Mobile Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Mobile number must be 20 characters or less");
				return false;
			}
			driver.setMobile(tmp);
		}

		tmp = line[i++].trim(); // Fax Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Fax number must be 20 characters or less");
				return false;
			}
			driver.setFax(tmp);
		}

		tmp = line[i++].trim(); // Address Line 1
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "Address line 1 must be 50 characters or less");
				return false;
			}
			driver.setAddress1(tmp);
		}

		tmp = line[i++].trim(); // Address Line 2
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "Address line 2 must be 50 characters or less");
				return false;
			}
			driver.setAddress2(tmp);
		}

		tmp = line[i++].trim(); // City
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "City must be 50 characters or less");
				return false;
			}
			driver.setCity(tmp);
		}

		tmp = line[i++].trim(); // State
		if (!tmp.isEmpty()) {
			if (tmp.length() != 2) {
				update(lineNum, i, "State must be 2 characters");
				return false;
			}
			driver.setState(tmp.toUpperCase());
		}

		tmp = line[i++].trim(); // Postal Code
		if (!tmp.isEmpty()) {
			if (tmp.length() > 10) {
				update(lineNum, i, "Postal code must be 10 characters or less");
				return false;
			}
			driver.setPostalCode(tmp);
		}

		tmp = line[i++].trim(); // Contact Name
		if (!tmp.isEmpty()) {
			if (tmp.length() > 100) {
				update(lineNum, i, "Contact name must be 100 characters or less");
				return false;
			}
			driver.setContactName(tmp);
		}

		tmp = line[i++].trim(); // Contact Relationship
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "Contact relationship must be 50 characters or less");
				return false;
			}
			driver.setContactRelationship(tmp);
		}

		tmp = line[i++].trim(); // Contact Phone Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Contact phone number must be 20 characters or less");
				return false;
			}
			driver.setContactPhone(tmp);
		}

		tmp = line[i++].trim(); // Contact Mobile Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Contact mobile number must be 20 characters or less");
				return false;
			}
			driver.setContactMobile(tmp);
		}

		String licenseNumber = null;
		tmp = line[i++].trim(); // Driver's License Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Driver's license number must be 20 characters or less");
				return false;
			}
			licenseNumber = tmp;
		}

		License license = null;

		String licenseState = null;
		tmp = line[i++].trim(); // Driver's License State
		if (!tmp.isEmpty()) {
			if (tmp.length() != 2) {
				update(lineNum, i, "Driver's license state must be 2 characters.");
				return false;
			}
			licenseState = tmp;
		}

		Date licenseExpiration = null;
		tmp = line[i++].trim(); // Driver's License Expiration Date
		if (!tmp.isEmpty()) {
			try {
				licenseExpiration = sdf.parse(tmp);
			} catch (ParseException x) {
				update(lineNum, i, "Invalid driver's license expiration date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Hazmat Expiration Date
		if (!tmp.isEmpty()) {
			try {
				driver.setHazmatExpiration(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid hazmat expiration date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Hire Date
		if (!tmp.isEmpty()) {
			try {
				cd.setHireDate(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid hire date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // MVR Date
		if (!tmp.isEmpty()) {
			try {
				driver.setMvrDate(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid MVR date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Medical Review Date
		if (!tmp.isEmpty()) {
			try {
				driver.setMedicalReviewDate(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid medical record date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Felony Conviction
		if (!tmp.isEmpty()) {
			if (tmp.equalsIgnoreCase("y")) {
				driver.setFelonyConviction(true);
			} else if (tmp.equalsIgnoreCase("n")) {
				driver.setFelonyConviction(false);
			} else {
				update(lineNum, i, "Expected Y or N, but found " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Felony Conviction Date
		if (!tmp.isEmpty()) {
			try {
				driver.setFelonyConvictionDate(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid felony conviction date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Felony Conviction Explanation
		if (!tmp.isEmpty()) {
			driver.setFelonyConvictionExplanation(tmp);
		}

		tmp = line[i++].trim(); // DUI Conviction
		if (!tmp.isEmpty()) {
			if (tmp.equalsIgnoreCase("y")) {
				driver.setDuiConviction(true);
			} else if (tmp.equalsIgnoreCase("n")) {
				driver.setDuiConviction(false);
			} else {
				update(lineNum, i, "Expected Y or N, but found " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // DUI Conviction Date
		if (!tmp.isEmpty()) {
			try {
				driver.setDuiConvictionDate(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid DUI conviction date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // DUI Conviction Explanation
		if (!tmp.isEmpty()) {
			driver.setDuiConvictionExplanation(tmp);
		}

		tmp = line[i++].trim(); // License Revoked
		if (!tmp.isEmpty()) {
			if (tmp.equalsIgnoreCase("y")) {
				driver.setLicenseRevoked(true);
			} else if (tmp.equalsIgnoreCase("n")) {
				driver.setLicenseRevoked(false);
			} else {
				update(lineNum, i, "Expected Y or N, but found " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // License Revoked Date
		if (!tmp.isEmpty()) {
			try {
				driver.setLicenseRevokedDate(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid license revoked date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // License Revoked Explanation
		if (!tmp.isEmpty()) {
			driver.setLicenseRevokedExplanation(tmp);
		}

		tmp = line[i++].trim(); // Controlled Substance
		if (!tmp.isEmpty()) {
			if (tmp.equalsIgnoreCase("y")) {
				driver.setControlledSubstance(true);
			} else if (tmp.equalsIgnoreCase("n")) {
				driver.setControlledSubstance(false);
			} else {
				update(lineNum, i, "Expected Y or N, but found " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Controlled Substance Date
		if (!tmp.isEmpty()) {
			try {
				driver.setControlledSubstanceDate(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid controlled substance date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Controlled Substance Explanation
		if (!tmp.isEmpty()) {
			driver.setControlledSubstanceExplanation(tmp);
		}

		tmp = line[i++].trim(); // Driver School
		if (!tmp.isEmpty()) {
			if (tmp.equalsIgnoreCase("y")) {
				driver.setDriverSchool(true);
			} else if (tmp.equalsIgnoreCase("n")) {
				driver.setDriverSchool(false);
			} else {
				update(lineNum, i, "Expected Y or N, but found " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Driver School Name
		if (!tmp.isEmpty()) {
			if (tmp.length() > 100) {
				update(lineNum, i, "Driver school name must be 100 characters or less");
				return false;
			}
			driver.setDriverSchoolName(tmp);
		}

		personDao.saveOrUpdate(person);
		driver.setPerson(person);
		driverDao.saveOrUpdate(driver);
		companyDriverDao.saveOrUpdate(cd);

		if (licenseNumber != null || licenseState != null || licenseExpiration != null) {
			if (licenseNumber == null) {
				update(lineNum, i - 3, "Driver's license number is required if any driver's license fields are populated");
				return false;
			}
			if (licenseState == null) {
				update(lineNum, i - 2, "Driver's license state is required if any driver's license fields are populated");
				return false;
			}
			if (licenseExpiration == null) {
				update(lineNum, i - 3, "Driver's license expiration is required if any driver's license fields are populated");
				return false;
			}
			license = licenseDao.getLicense(driver, licenseState, licenseNumber);
			if (license == null) {
				license = new License();
			}
			license.setDriver(driver);
			license.setState(licenseState);
			license.setNumber(licenseNumber);
			license.setExpiration(licenseExpiration);
		}

		licenseDao.saveOrUpdate(license);
		return true;
	}

	private boolean importTruck(int lineNum, DataImport di, String[] line) {
		if (line.length != TRUCK_FIELDS) {
			update("Invalid number of fields on line " + lineNum + ". Expected " + TRUCK_FIELDS + " fields, but found " + line.length);
			return false;
		}
		Truck truck;
		CompanyTruck ct;
		CompanyDriver cd;
		DriverTruck dt = null;

		int i = 0;

		String tmp = line[i++]; // Truck Number
		if (tmp.isEmpty()) {
			update(lineNum, i, "Truck number is required");
			return false;
		}
		if (tmp.length() > 100) {
			update(lineNum, i, "Truck number must be 100 characters or less");
			return false;
		}
		tmp = tmp.toUpperCase();
		truck = truckDao.getByTruckNumber(di.getCompany(), tmp);
		// We can only continue with an existing truck if we have previously imported in this job or if overwrite is true
		if (truck != null && !di.isOvewrite() && !importedTrucks.contains(tmp)) {
			update(lineNum, i, "Truck with number " + tmp + " already exists");
			return false;
		}
		if (truck != null) {
			ct = companyTruckDao.get(di.getCompany(), truck);
		} else {
			truck = new Truck();
			ct = new CompanyTruck(di.getCompany(), truck);
		}
		importedTrucks.add(tmp);
		ct.setTruckNumber(tmp);

		tmp = line[i++].trim(); // Driver Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 100) {
				update(lineNum, i, "Driver number must be 100 characters or less");
				return false;
			}
			tmp = tmp.toUpperCase();
			cd = companyDriverDao.get(di.getCompany(), tmp);
			if (cd == null) {
				update(lineNum, i, "Driver " + tmp + " was not found");
				return false;
			}
			if (truck.getId() != null) {
				dt = driverTruckDao.get(cd.getDriver(), truck);
			}
			if (dt == null) {
				dt = new DriverTruck(cd. getDriver(), truck);
				cd.setDriverNumber(tmp);
			}
		}

		tmp = line[i++].trim(); // Active
		if (!tmp.isEmpty()) {
			if (tmp.equalsIgnoreCase("y")) {
				truck.setActive(true);
			} else if (tmp.equalsIgnoreCase("n")) {
				truck.setActive(false);
			} else {
				update(lineNum, i, "Expected Y or N, but found " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // VIN
		if (tmp.isEmpty()) {
			update(lineNum, i, "VIN number is required");
			return false;
		}
		if (tmp.length() > 100) {
			update(lineNum, i, "VIN number must be 100 characters or less");
			return false;
		}
		tmp = tmp.toUpperCase();
		if (!tmp.isEmpty()) {
			truck.setVin(tmp);
		}

		tmp = line[i++].trim(); // Year
		if (!tmp.isEmpty()) {
			try {
				truck.setYear(Integer.parseInt(tmp));
			} catch (NumberFormatException x) {
				update(lineNum, i, "Invalid year " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Make
		if (!tmp.isEmpty()) {
			if (tmp.length() > 30) {
				update(lineNum, i, "Make must be 30 characters or less");
				return false;
			}
			truck.setMake(tmp);
		}

		tmp = line[i++].trim(); // Model
		if (!tmp.isEmpty()) {
			if (tmp.length() > 30) {
				update(lineNum, i, "Model must be 30 characters or less");
				return false;
			}
			truck.setModel(tmp);
		}

		tmp = line[i++].trim(); // License Plate
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "License plate must be 20 characters or less");
				return false;
			}
			truck.setLicense(tmp);
		}

		tmp = line[i++].trim(); // Registration Date
		if (!tmp.isEmpty()) {
			try {
				truck.setRegistration(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid registration date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Annual Inspection Date
		if (!tmp.isEmpty()) {
			try {
				truck.setAnnualInspection(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid annual inspection date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Bobtail Insurance Date
		if (!tmp.isEmpty()) {
			try {
				truck.setBobtailInsurance(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid bobtail insurance date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // IFTA (boolean)
		if (!tmp.isEmpty()) {
			if (tmp.equalsIgnoreCase("y")) {
				truck.setIfta(true);
			} else if (tmp.equalsIgnoreCase("n")) {
				truck.setIfta(false);
			} else {
				update(lineNum, i, "Expected Y or N, but found " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Quarterly Maintenance Date
		if (!tmp.isEmpty()) {
			try {
				truck.setQuarterlyMaintenance(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid quarterly maintenance date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Physical Damage Insurance
		if (!tmp.isEmpty()) {
			try {
				truck.setPhysicalDamageInsurance(sdf.parse(tmp));
			} catch (ParseException x) {
				update(lineNum, i, "Invalid Physical Damage Insurance date: " + tmp);
				return false;
			}
		}

		tmp = line[i++].trim(); // Lessor Number
		if (!tmp.isEmpty()) {
			if (tmp.length() > 100) {
				update(lineNum, i, "Lessor number must be 100 characters or less");
				return false;
			}
			truck.setLessorNumber(tmp);
		}

		tmp = line[i++].trim(); // Lessor Name
		if (!tmp.isEmpty()) {
			if (tmp.length() > 100) {
				update(lineNum, i, "Lessor name must be 100 characters or less");
				return false;
			}
			truck.setLessorName(tmp);
		}

		tmp = line[i++].trim(); // Lessor Address Line 1
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "Lessor address line 1 must be 50 characters or less");
				return false;
			}
			truck.setLessorAddress1(tmp);
		}

		tmp = line[i++].trim(); // Lessor Address Line 2
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "Lessor address line 2 must be 50 characters or less");
				return false;
			}
			truck.setLessorAddress2(tmp);
		}

		tmp = line[i++].trim(); // Lessor City
		if (!tmp.isEmpty()) {
			if (tmp.length() > 50) {
				update(lineNum, i, "Lessor city must be 50 characters or less");
				return false;
			}
			truck.setLessorCity(tmp);
		}

		tmp = line[i++].trim(); // Lessor State
		if (!tmp.isEmpty()) {
			if (tmp.length() != 2) {
				update(lineNum, i, "Lessor state mut be 2 characters.");
				return false;
			}
			truck.setLessorState(tmp.toUpperCase());
		}

		tmp = line[i++].trim(); // Lessor Postal Code
		if (!tmp.isEmpty()) {
			if (tmp.length() > 10) {
				update(lineNum, i, "Lessor postal code must be 10 characters or less.");
				return false;
			}
			truck.setLessorPostalCode(tmp);
		}

		tmp = line[i++].trim(); // Lessor Phone
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Lessor phone must be 20 characters or less.");
				return false;
			}
			truck.setLessorPhone(tmp);
		}

		tmp = line[i++].trim(); // Lessor Mobile
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Lessor mobile must be 20 characters or less.");
				return false;
			}
			truck.setLessorMobile(tmp);
		}

		tmp = line[i++].trim(); // Lessor Government ID
		if (!tmp.isEmpty()) {
			if (tmp.length() > 20) {
				update(lineNum, i, "Lessor government ID must be 20 characters or less.");
				return false;
			}
			truck.setLessorGovId(tmp);
		}
		truckDao.saveOrUpdate(truck);
		driverTruckDao.saveOrUpdate(dt);
		companyTruckDao.saveOrUpdate(ct);

		return true;
	}

	@Override
	public void doExecute(JobExecutionContext ctx) throws JobExecutionException {
		log.trace("DataImport job started, checking for data needing import");
		new TransactionTemplate(transactionManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				sb = new StringBuilder();
				boolean success = false;
				try {
					di = dataImportDao.getImportToProcess();
					if (di != null) {
						log.info("Starting data import job");

						update(new Date());
						update("Starting data import");

						CSVReader reader = new CSVReader(new StringReader(di.getData()), ',', '"', 1);
						List<String[]> list = reader.readAll();
						update("Found " + list.size() + " uploaded records.");
						update("Skipping line 1 as a header");
						int num = 2;
						for (String[] line: list) {
							update("Processing row " + num);
							if (di.getImportType() == ImportType.DRIVER) {
								success = importDriver(num, di, line);
							} else { // Truck
								success = importTruck(num, di, line);
							}
							if (!success) {
								return;
							}
							num++;
						}
						success = true;
						log.info("Data import job has completed");
					}
				} catch (Throwable t) {
					status.setRollbackOnly();
					log.error("Unexpected exception in data import job: " + t.getMessage(), t);
					if (di != null) {
						success = false;
						update("Error: " + t.getMessage());
						update(ThrowableHelper.getStackTrace(t));
					}
				} finally {
					if (di != null) {
						if (!success) {
							status.setRollbackOnly();
							update("Data import FAILED");
							update(new Date(), false);
						} else {
							update("Data import completed successfully");
							update(new Date(), true);
						}
					}
				}
			}
		});
	}
}
