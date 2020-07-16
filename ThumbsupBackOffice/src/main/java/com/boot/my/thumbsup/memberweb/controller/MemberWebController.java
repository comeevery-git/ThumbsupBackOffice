package com.boot.my.thumbsup.memberweb.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberWebController {
 
	@Autowired
	UserDetailsService userService;
	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	 * 공지사항 관리 이동
	 */
	@GetMapping("/notice")
	public String notice(Model model) {
		return "member/notice";
	}
	/*
	 * 홈페이지 관리 이동
	 */
    @GetMapping("/test123")
    public String test123(Model model) {
    	return "homepage/test123";
    }
    
    
}
