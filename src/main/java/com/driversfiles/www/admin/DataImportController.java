package com.driversfiles.www.admin;

import com.driversfiles.www.core.controller.BaseController;
import com.driversfiles.www.core.dao.CompanyDao;
import com.driversfiles.www.core.dao.DataImportDao;
import com.driversfiles.www.core.data.DataImport;
import com.driversfiles.www.core.service.ImportType;
import com.driversfiles.www.quartz.JobService;
import com.driversfiles.www.util.IOHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * Handles data mass import requests from admin users.
 *
 * @author Erik R. Jensen
 */
@Controller
public class DataImportController extends BaseController {

	@Autowired
	@Qualifier("companyDao")
	private CompanyDao companyDao;

	@Autowired
	@Qualifier("dataImportDao")
	private DataImportDao dataImportDao;

	@Autowired
	@Qualifier("jobService")
	private JobService jobService;

	@RequestMapping(value = "/secure/admin/imports", method = RequestMethod.GET)
	public ModelAndView imports() {
		return new ModelAndView("admin_imports.page", "imports", dataImportDao.find("createdDate", false));
	}

	private void prep(ModelAndView mav) {
		mav.addObject("importTypes", ImportType.values());
		mav.addObject("companies", companyDao.getCompanies());
	}

	@RequestMapping(value = "/secure/admin/imports/new", method = RequestMethod.GET)
	public ModelAndView newImport(@ModelAttribute("importForm") ImportForm form) {
		ModelAndView m = new ModelAndView("admin_import.page");
		prep(m);
		return m;
	}

	@RequestMapping(value = "/secure/admin/imports/new", method = RequestMethod.POST)
	public ModelAndView newImportPost(@ModelAttribute("importForm") @Valid ImportForm form, Errors errors) throws IOException {
		ModelAndView m  = new ModelAndView();
		if (form.getFile() != null && form.getFile().isEmpty()) {
			errors.rejectValue("file", "NotEmpty");
		}
		if (errors.hasErrors()) {
			prep(m);
			m.setViewName("admin_import.page");
			return m;
		}
		DataImport di = new DataImport();
		di.setImportType(form.getImportType());
		di.setCompany(companyDao.find(form.getCompanyId()));
		InputStream in = null;
		try {
			in = form.getFile().getInputStream();
			di.setData(IOHelper.readString(in, "utf-8"));
		} finally {
			IOHelper.close(in);
		}
		di.setOvewrite(form.isOverwrite());
		dataImportDao.save(di);
		dataImportDao.flush();
		jobService.execute("dataImportJob");
		m.setViewName("redirect:/secure/admin/imports");
		return m;
	}

	@RequestMapping(value = "/secure/admin/imports/{id}", method = RequestMethod.GET)
	public ModelAndView viewImport(@PathVariable Long id) {
		return new ModelAndView("admin_import_view.page", "imp", dataImportDao.find(id));
	}

	@RequestMapping(value = "/secure/admin/imports/{id}.csv", method = RequestMethod.GET)
	public void viewData(@PathVariable Long id, HttpServletResponse res, PrintWriter out) throws IOException {
		DataImport di = dataImportDao.find(id);
		res.setContentType("text/plain");
		res.setContentLength(di.getData().getBytes("utf-8").length);
		out.write(di.getData());
	}

	@RequestMapping(value = "/secure/admin/imports/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id) {
		dataImportDao.delete(dataImportDao.find(id));
		return "redirect:/secure/admin/imports?message=success";
	}

	@RequestMapping(value = "/secure/admin/imports/{id}.json", method = RequestMethod.GET)
	@ResponseBody
	public DataImportModel getDataImport(@PathVariable Long id) {
		DataImport di = dataImportDao.find(id);
		DataImportModel m = new DataImportModel();
		m.setSuccess(di.getSuccess());
		SimpleDateFormat fmt = new SimpleDateFormat("hh:mm:ss a");
		if (di.getStartTime() != null) {
			m.setStartTime(fmt.format(di.getStartTime()));
		}
		if (di.getEndTime() != null) {
			m.setEndTime(fmt.format(di.getEndTime()));
		}
		m.setLog(di.getLog());
		return m;
	}

	public static class DataImportModel {

		private Boolean success;
		private String startTime;
		private String endTime;
		private String log;

		public Boolean getSuccess() {
			return success;
		}

		public void setSuccess(Boolean success) {
			this.success = success;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getLog() {
			return log;
		}

		public void setLog(String log) {
			this.log = log;
		}
	}

	public static class ImportForm {

		@NotNull
		private ImportType importType;

		@NotNull
		private Long companyId;

		@NotNull
		private MultipartFile file;

		private boolean overwrite = false;

		public ImportType getImportType() {
			return importType;
		}

		public void setImportType(ImportType importType) {
			this.importType = importType;
		}

		public Long getCompanyId() {
			return companyId;
		}

		public void setCompanyId(Long companyId) {
			this.companyId = companyId;
		}

		public MultipartFile getFile() {
			return file;
		}

		public void setFile(MultipartFile file) {
			this.file = file;
		}

		public boolean isOverwrite() {
			return overwrite;
		}

		public void setOverwrite(boolean overwrite) {
			this.overwrite = overwrite;
		}
	}
}
