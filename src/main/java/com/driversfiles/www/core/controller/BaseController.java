package com.driversfiles.www.core.controller;

import com.driversfiles.www.core.NotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Base class for all controllers.
 *
 * @author Erik R. Jensen
 */
public class BaseController {

	@ExceptionHandler(NotFoundException.class)
	public void handleNotFound(HttpServletResponse res) throws IOException {
		res.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
