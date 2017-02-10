package com.driversfiles.www.company;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.driversfiles.www.auth.AuthService;
import com.driversfiles.www.common.TrucksController;
import com.driversfiles.www.core.dao.CompanyDao;
import com.driversfiles.www.core.data.Company;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.State;
import com.driversfiles.www.fs.FileStoreService;
import com.driversfiles.www.fs.ImageInfo;
import com.driversfiles.www.fs.Scalr;
import com.driversfiles.www.fs.Scalr.Method;

import edu.emory.mathcs.backport.java.util.Arrays;


/**
 * Handles My Company requests.
 *
 * @author Mark Burns
 */
@Controller
public class MyCompanyController {

	private static final Logger log = LoggerFactory.getLogger(TrucksController.class);
	private static final String[] VALID_FORMAT_NAMES = {"JPEG", "GIF", "PNG"};
	private static final Integer LOGO_MAX_HEIGHT = 250;
	private static final Integer LOGO_MAX_WIDTH = 250;

	@Autowired
	private AuthService authService;
	
	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private FileStoreService fileStoreService;

	
	@ModelAttribute("states")
	State[] getStates() {
		return State.values();
	}

	@RequestMapping(value = "/secure/company/company", method = RequestMethod.GET)
	public String myCompany(@ModelAttribute("myCompanyForm") MyCompanyForm form, HttpServletRequest req) {
		
		// Gather info for this page
		Person person = authService.getEffectiveUser();
		Company company = person.getCompany();
		if (company != null) {
			form.setCompanyName(company.getName());
			form.setCompanyNumber(company.getCompanyNumber());
			form.setAddress1(company.getAddress1());
			form.setAddress2(company.getAddress2());
			form.setCity(company.getCity());
			form.setState(company.getState());
			form.setPostalCode(company.getPostalCode());
			form.setPhone(company.getPhone());
			form.setFax(company.getFax());
			form.setWebsite(company.getWebsite());
			
			// See if they have an icon uploaded
			String iconPath = "company/icon/" + company.getUuid();
			if (fileStoreService.exists(iconPath)) {
				String uri = String.format("/secure/company/%s/icon", company.getUuid());
				form.setIconUrl(uri);
			}
		}
		
		return "mycompany.page";
	}
	
	@RequestMapping(value = "/secure/company/{companyUuid}/icon", method = RequestMethod.GET)
	public void documents(@PathVariable String companyUuid, HttpServletRequest req, HttpServletResponse res) {
		
		// Stream the doc to the browser
		try {
			Company company = companyDao.getByUuid(companyUuid);

			fileStoreService.writeFile(res, "company/icon/"+company.getUuid(), "image/gif", "company.gif");
		} catch (Exception e) {
			log.error("Exception while trying to download a stored company icon", e);
		}
	}

	@RequestMapping(value = "/secure/company/company/save", method = RequestMethod.POST)
	public String saveCompany(@ModelAttribute("myCompanyForm") @Valid MyCompanyForm form, Errors errors, HttpServletRequest req) {
		
		// Handle validation errors
		if (errors.hasErrors()) {
			return "mycompany.page";
		}
		
		Person person = authService.getEffectiveUser();
		Company company = person.getCompany();
		
		if (company != null) {
			company.setName(form.getCompanyName());
			company.setAddress1(form.getAddress1());
			company.setAddress2(form.getAddress2());
			company.setCity(form.getCity());
			company.setState(form.getState());
			company.setPostalCode(form.getPostalCode());
			company.setPhone(form.getPhone());
			company.setFax(form.getFax());
			company.setWebsite(form.getWebsite());
			companyDao.update(company);
			
			// If icon to upload then do it
			MultipartFile iconFile = form.getIconFile();
			if (iconFile != null && iconFile.getSize() > 0) {

				if (!isValidIconFile(iconFile)) {
					errors.rejectValue("iconFile", "icon.invalid");
					return "mycompany.page";
				}
				
				try {
					// Resize file and save
					saveCompanyLogo(company.getUuid(), iconFile);
				} catch (Exception e) {
					log.error("Error in MyCompanyController.saveCompany", e);
					errors.rejectValue("iconFile", "There was a severe error while uploading the file!");
					return "mycompany.page";
				}
			}
		}
	
		return "redirect:/secure/company/company?message=success";
	}
	
	/**
	 * Test whether the upload file is valid for use as a company logo.
	 * The file must be less than 5MB and be one of the following types:
	 * PNG, GIF, JPG.
	 * 
	 * @param iconFile
	 * @return
	 */
	private boolean isValidIconFile(MultipartFile iconFile) {
		
		if (iconFile == null || iconFile.getSize() > 5242880L) {
			return false;
		}
		
		try {
			ImageInfo info = new ImageInfo();
			info.setInput(iconFile.getInputStream());
			
			if (info.check()) {
				@SuppressWarnings("unchecked")
				List<String> validTypes = Arrays.asList(VALID_FORMAT_NAMES);
				String type = info.getFormatName();
				if (type != null && validTypes.contains(type)) {
					return true;
				}
			}
			
		} catch (IOException e) {
			log.error("Error in MyCompanyController.isValidIconFile", e);
		}
		
		return false;
	}

	/**
	 * Upload the image, scale it to fit max dimensions, and save the scaled
	 * file. The original is discarded.
	 * 
	 * @param uuid
	 * @param iconFile
	 * @throws Exception
	 */
	private void saveCompanyLogo(String uuid, MultipartFile iconFile) throws Exception {
		
		InputStream imageStream = iconFile.getInputStream();
		BufferedImage imageBuffer = ImageIO.read(imageStream);
		
		BufferedImage scaledImage = Scalr.resize(imageBuffer, Method.BALANCED, LOGO_MAX_WIDTH, LOGO_MAX_HEIGHT);
		imageBuffer.flush();

		fileStoreService.saveImage("company/icon/" + uuid, "jpg", scaledImage);
		scaledImage.flush();
	}

	public static class MyCompanyForm {
		
		@NotEmpty
		private String companyName;
		private String companyNumber;
		private String iconUrl;
		private MultipartFile iconFile;
		private String address1;
		private String address2;
		private String city;
		private String state;
		private String postalCode;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String phone;
		@Pattern(regexp = "[\\d{1}-]*\\d{3}-\\d{3}-\\d{4}|^$")
		private String fax;
		private String website;
		
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
		public String getIconUrl() {
			return iconUrl;
		}
		public void setIconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
		}
		public MultipartFile getIconFile() {
			return iconFile;
		}
		public void setIconFile(MultipartFile iconFile) {
			this.iconFile = iconFile;
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
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getFax() {
			return fax;
		}
		public void setFax(String fax) {
			this.fax = fax;
		}
		public String getWebsite() {
			return website;
		}
		public void setWebsite(String website) {
			this.website = website;
		}
	}
	
}
