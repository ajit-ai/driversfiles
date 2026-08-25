package com.driversfiles.www.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppErrorController implements ErrorController {

	private static final java.util.Set<Integer> CUSTOM_PAGES =
			new java.util.HashSet<>(java.util.Arrays.asList(401, 403, 404, 500));

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, HttpServletResponse response) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		int code = status != null ? Integer.parseInt(status.toString()) : 500;
		if (CUSTOM_PAGES.contains(code)) {
			try {
				response.setStatus(code);
				request.getRequestDispatcher("/resources/error-pages/" + code + ".jsp")
						.forward(request, response);
				return null;
			} catch (Exception x) {
				return null;
			}
		}
		response.setStatus(code);
		return null;
	}
}
