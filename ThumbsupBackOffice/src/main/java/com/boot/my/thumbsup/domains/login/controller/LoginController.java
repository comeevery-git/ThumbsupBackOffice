package com.boot.my.thumbsup.domains.login.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.my.thumbsup.common.RqsAPI;
import com.boot.my.thumbsup.domains.Admin.domain.Admin;
import com.boot.my.thumbsup.domains.login.domain.RSPAUTH001;
import com.boot.my.thumbsup.domains.login.domain.RSPAUTH002;

@Controller
@RequestMapping("/login")
public class LoginController {
 
	@Autowired
	RqsAPI rqsApi;
	
	@Autowired
	UserDetailsService userService;
	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	 * 직원 로그인/가입으로 이동
	 */
    @GetMapping("/admin_login")
    public String admin_login(Model model) {
    	return "login/admin_login";
    }
    @GetMapping("/admin_register")
    public String admin_register(Model model) {
    	return "login/admin_register";
    }
    
    /*
     * 직원 로그인
     */
    @PostMapping(value="/userLogin")
    @ResponseBody
    public ModelAndView UserLogin(
    			ModelAndView mv,
    			String adminId,
    			String adminPwd,
    			RestTemplate restTemplate,
    			RedirectAttributes redirectAttributes,
    			HttpServletRequest request
    		) {
    	System.out.println("BackOffice --- Login, API로 요청");

		//요청 url
		String url = "http://localhost:8007/auth/signin_m";
		//요청 카테고리
		String sendCategory = "login";
		//요청값
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("adminId", adminId);
		map.add("adminPwd", adminPwd);
		
		//API요청
		Map<String, Object> result_map = rqsApi.sendAPI(url, map, restTemplate, sendCategory);
		//API응답값
		ResponseEntity<RSPAUTH001> response = (ResponseEntity<RSPAUTH001>) result_map.get("response");
		//System.out.println("#응답# "+response);
		
		//token, 권한 확인
		String token = "";
		String role = "";
		if(response.getBody().getAdminToken() != null) {
			token = response.getBody().getAdminToken();
			role = response.getBody().getAdminRole();
		}
		
		//token, 권한 세팅
		HttpSession session = request.getSession();
		session.setAttribute("token", token);
		session.setAttribute("role", role);
		System.out.println("TOKEN : " + session.getAttribute("token"));
		System.out.println("ROLE : " + session.getAttribute("role"));
		
		// 응답 상태에 따른 처리
		String result = response.getBody().getSuccess();
		System.out.println(response.getBody().getMsg());
		// 로그인 성공
		if(result.equals("success")) {
			redirectAttributes.addFlashAttribute( "msg", "환영합니다!" );
			redirectAttributes.addFlashAttribute( "loginRslt", "success_login" );
			return new ModelAndView("redirect:/index");
			
		// 로그인 실패
		} else {
			redirectAttributes.addFlashAttribute( "msg", "아이디 또는 비밀번호를 확인해주세요." );
			redirectAttributes.addFlashAttribute( "loginRslt", "fail" );
			return new ModelAndView("redirect:/login/admin_login");
			
		}
		
    }


    /*
     * 직원가입
     */
    @RequestMapping(value="/userRegister")
    @ResponseBody
    public ModelAndView UserRegister(
    			ModelAndView mv,
    			String adminId,
    			String adminPwd,
    			String adminNm,
    			String adminRrno,
    			String adminTel,
    			String adminDepart,
    			RestTemplate restTemplate,
    			RedirectAttributes redirectAttributes
    		) {

    	System.out.println("BackOffice --- Register, API로 요청");

		//요청 url
		String url = "http://localhost:8007/auth/signup_m";
		String sendCategory = "register";
		//요청값
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("adminId", adminId);
		map.add("adminPwd", adminPwd);
		map.add("adminNm", adminNm);
		map.add("adminRrno", adminRrno);
		map.add("adminTel", adminTel);
		map.add("adminDepart", adminDepart);

		//API요청
		Map<String, Object> result_map = rqsApi.sendAPI(url, map, restTemplate, sendCategory);
		//API응답값
		ResponseEntity<RSPAUTH002> response = (ResponseEntity<RSPAUTH002>) result_map.get("response");

		// 응답 상태에 따른 처리
		String result = response.getBody().getSuccess();
		System.out.println(response.getBody().getMsg());
		if(result.equals("success")) {
			redirectAttributes.addFlashAttribute( "msg", "가입이 완료되었습니다." );
			redirectAttributes.addFlashAttribute( "loginRslt", "success_reg" );
			return new ModelAndView("redirect:/index");
		} else {
			redirectAttributes.addFlashAttribute( "msg", "다시 시도해 주세요." );
			redirectAttributes.addFlashAttribute( "loginRslt", "fail" );
			return new ModelAndView("redirect:/index");
		}
		
    }
    
    /*
     * 로그아웃
     */
    @GetMapping(value="/logout")
    public String logout(HttpServletRequest request) {
    	System.out.println("Admin Logout.....");
    	HttpSession session = request.getSession();
    	session.invalidate();
    	
    	return "redirect:/login/admin_login";
    }

    /*
     * 비밀번호 찾기 미완
     */
    @GetMapping("/admin_forget_pwd")
    public String admin_forget_pwd(Model model) {
    	return "login/admin_forget_pwd";
    }
    
}
