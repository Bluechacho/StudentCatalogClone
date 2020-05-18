package com.fdmgroup.StudentCatalogSpring;

import java.util.ArrayList;

import javax.servlet.http.*;

import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.businesslogic.*;

@Controller
public class CatalogController {

	// Create the StudentList attribute, which contains
	// the results from the last database query
	@ModelAttribute("studentList")
	public ArrayList<QueryResult> createStudentList(String firstname, String lastname, String ssn, boolean searchMode, boolean writeMode) {
		ArrayList<QueryResult> studentList = new ArrayList<QueryResult>();
		ServerConnector.setFirst(firstname);
		ServerConnector.setLast(lastname);
		ServerConnector.setSSN(ssn);
		ServerConnector.setsearchMode(searchMode);
		ServerConnector.setwriteMode(writeMode);
		studentList = ServerConnector.query();
		return studentList;
	}

	// Load the correct view for the current user role
	@GetMapping({ "/", "/search*", "/write*" })
	public String loadView(@RequestParam(required = false, defaultValue = "User") String name, Model model,
			HttpServletRequest req) {
		String firstname = "$def";
		String lastname = "$def";
		String ssn = "$def";
		boolean searchMode = false;
		boolean writeMode = false;

		// Retrieve user name, user role, search path
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userRole = (UserDetails) auth.getPrincipal();
		HttpServletRequest request = (HttpServletRequest) req;
		String path = request.getRequestURI().substring(request.getContextPath().length());

		// Process path: extract values, then run the
		// search query of the path (if applicable)
		if (path.contains("/search")) {
			String[] splitTemp1 = path.split("\\.");
			String[] splitTemp2;

			splitTemp2 = splitTemp1[1].split("=");
			firstname = splitTemp2[1];

			splitTemp2 = splitTemp1[2].split("=");
			lastname = splitTemp2[1];

			splitTemp2 = splitTemp1[3].split("=");
			ssn = splitTemp2[1];
			
			splitTemp2 = splitTemp1[4].split("=");
			searchMode = true;
			
			splitTemp2 = splitTemp1[5].split("=");
			writeMode = false;
		}
		
		// Process path: extract values, then run the
		// write query of the path (if applicable)
		if (path.contains("/write")) {
			String[] splitTemp1 = path.split("\\.");
			String[] splitTemp2;

			splitTemp2 = splitTemp1[1].split("=");
			firstname = splitTemp2[1];

			splitTemp2 = splitTemp1[2].split("=");
			lastname = splitTemp2[1];

			splitTemp2 = splitTemp1[3].split("=");
			ssn = splitTemp2[1];
			
			splitTemp2 = splitTemp1[4].split("=");
			searchMode = false;
			
			splitTemp2 = splitTemp1[5].split("=");
			writeMode = true;
		}
		model.addAttribute("name", auth.getName());
		model.addAttribute("studentList", createStudentList(firstname, lastname, ssn, searchMode, writeMode));

		switch (userRole.getAuthorities().toString()) {
		case "[ROLE_STUDENT]":
			return "student-view";
		case "[ROLE_PROFESSOR]":
			return "professor-view";
		case "[ROLE_ADMIN]":
			return "admin-view";
		}
		return "student-view";
	}

	// Log the current user out and return to login page
	@GetMapping({ "/logout" })
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}
}