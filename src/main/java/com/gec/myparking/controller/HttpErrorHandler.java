package com.gec.myparking.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HttpErrorHandler implements ErrorController {
	private final static String ERROR_PATH = "/error";

	/**
	 * Supports the HTML Error View
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ERROR_PATH, produces = "text/html")
	public String errorHtml(HttpServletRequest request) {
		return "404";
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
