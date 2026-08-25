package com.driversfiles.www.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, HttpServletResponse response) {
		Object status = request.getAttribute(jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE);
		int code = status != null ? Integer.parseInt(status.toString()) : 500;
		response.setStatus(code);
		response.setContentType("text/html;charset=UTF-8");
		String title = switch (code) {
			case 401 -> "Sign In Required";
			case 403 -> "Access Denied";
			case 404 -> "Page Not Found";
			default -> "Something Went Wrong";
		};
		try {
			response.getWriter().write("""
				<!DOCTYPE html>
				<html lang="en"><head><meta charset="utf-8"><title>%d %s</title>
				<style>body{background:#f4f6f8;font-family:'Segoe UI',system-ui,sans-serif}
				.c{max-width:520px;margin:12vh auto;background:#fff;border-radius:14px;
				box-shadow:0 10px 30px rgba(0,0,0,.12);padding:48px;text-align:center}
				.code{font-size:4rem;font-weight:800;color:#f18700}h1{font-size:1.25rem;color:#2c3e50}
				a{display:inline-block;margin-top:16px;padding:10px 22px;background:#f18700;color:#fff;
				border-radius:8px;text-decoration:none}</style></head>
				<body><div class="c"><div class="code">%d</div><h1>%s</h1>
				<a href="/">Back to Home</a></div></body></html>
				""".formatted(code, title, title, code));
		} catch (Exception ignored) {
		}
		return null;
	}
}
