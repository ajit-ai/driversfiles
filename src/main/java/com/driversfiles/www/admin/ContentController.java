package com.driversfiles.www.admin;

import com.driversfiles.www.core.dao.ContentNodeDao;
import com.driversfiles.www.core.data.ContentNode;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles content management requests.
 *
 * @author Erik R. Jensen
 */
@Controller
public class ContentController {

	@Autowired
	@Qualifier("contentNodeDao")
	private ContentNodeDao dao;

	@RequestMapping("/secure/admin/content/nodes")
	public ModelAndView listNodes() {
		return new ModelAndView("admin_content_nodes.page", "nodes", dao.find("name", true));
	}

	@RequestMapping(value = "/secure/admin/content/nodes/{name}", method = RequestMethod.GET)
	public ModelAndView editNode(@PathVariable("name") String name, @ModelAttribute("contentForm") ContentForm form) {
		ContentNode cn = dao.findByName(name);
		form.setContent(cn.getContent());
		return new ModelAndView("admin_content_node.page", "node", cn);
	}

	@RequestMapping(value = "/secure/admin/content/nodes/{name}", method = RequestMethod.POST)
	public ModelAndView editNodePost(@PathVariable("name") String name, @ModelAttribute("contentForm") ContentForm form,
			Errors errors) {
		ModelAndView mav = new ModelAndView();
		if (errors.hasErrors()) {
			mav.setViewName("admin_content_node.page");
			mav.addObject("node", dao.findByName(name));
			return mav;
		}
		ContentNode cn = dao.findByName(name);
		cn.setContent(form.getContent());
		dao.update(cn);
		mav.setViewName("redirect:/secure/admin/content/nodes?message=success");
		return mav;
	}

	public static class ContentForm {

		@NotEmpty
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

}
