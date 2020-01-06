package com.example.ajax.api.controller.vo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/home")
	public String home() {
		return "home"; // return html page with name home
	}
	
	@GetMapping("/libraryMember")
	public String libraryMember() {
		return "libraryMember"; // return html page with name home
	}
}
