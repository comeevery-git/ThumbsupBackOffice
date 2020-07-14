package com.boot.my.thumbsup.domains.login.controller;


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

import com.boot.my.thumbsup.domains.login.domain.RSPAUTH001;
import com.boot.my.thumbsup.domains.login.domain.RSPAUTH002;

@Controller
@RequestMapping("/login")
public class LoginController {
 
	@Autowired
	UserDetailsService userService;
	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	 * 직원 로그인, 직원가입
	 */
    @GetMapping("/admin_login")
    public String admin_login(Model model) {
    	return "login/admin_login";
    }
    @GetMapping("/admin_register")
    public String admin_register(Model model) {
    	return "login/admin_register";
    }
    
    @PostMapping(value="/userLogin")
    @ResponseBody
    public ModelAndView UserLogin(
    			ModelAndView mv,
    			String url,
    			String adminId,
    			String adminPwd,
    			RestTemplate restTemplate,
    			RedirectAttributes redirectAttributes,
    			HttpServletRequest request,
    			@RequestBody String msg
    		) {
    	System.out.println("member --- msg@@@@@@@@@@@@@@@@@ : "+msg);
    	System.out.println("member --- adminId : "+adminId);
    	System.out.println("member --- adminPwd : "+adminPwd);
    	System.out.println("member --- Login, API로 요청");
    	
    	// API 데이터 요청 및 응답
		HttpHeaders headers = new HttpHeaders();
		//Charset utf8 = Charset.forName("UTF-8");
		//MediaType mediaType = new MediaType("application","json",utf8);
		//headers.setContentType(mediaType);
		System.out.println("member --- #headers# "+headers);

		//요청 url
		url = "http://localhost:8007/auth/signin_m";
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("adminId", adminId);
		map.add("adminPwd", adminPwd);

		HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		System.out.println("member --- #entity# "+entity);
		
		
		ResponseEntity<RSPAUTH001> response = restTemplate.postForEntity(url, entity, RSPAUTH001.class);
		String data = response.getBody().getData();
		String result = response.getBody().getSuccess();
		
		//token 세팅
		HttpSession session = request.getSession();
		session.setAttribute("token", data);
		System.out.println(session);
		System.out.println(session.getAttribute("token"));


		// 응답 상태에 따른 처리
		if(result.equals("true")) {
			redirectAttributes.addFlashAttribute( "msg", "환영합니다!" );
			redirectAttributes.addFlashAttribute( "loginRslt", "success_login" );
			return new ModelAndView("redirect:/index");
		} else {
			redirectAttributes.addFlashAttribute( "msg", "아이디 또는 비밀번호를 확인해주세요." );
			redirectAttributes.addFlashAttribute( "loginRslt", "fail" );
			return new ModelAndView("redirect:/index");
		}
    }


    /*
     * 직원가입
     */
    @RequestMapping(value="/userRegister")
    @ResponseBody
    public ModelAndView UserRegister(
    			ModelAndView mv,
    			String url,
    			String adminId,
    			String adminPwd,
    			String adminNm,
    			String adminRrno,
    			String adminTel,
    			String adminDepart,
    			RestTemplate restTemplate,
    			RedirectAttributes redirectAttributes,
    			@RequestBody String msg
    		) {

    	System.out.println("admin --- Register, API로 요청");
    	// API 데이터 요청 및 응답
		try {
			HttpHeaders headers = new HttpHeaders();
			//Charset utf8 = Charset.forName("UTF-8");
			//MediaType mediaType = new MediaType("application","json",utf8);
			//headers.setContentType(mediaType);
			System.out.println("member --- #headers# "+headers);
	
			//요청 url
			url = "http://localhost:8007/auth/signup_m";
			
			MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
			map.add("adminId", adminId);
			map.add("adminPwd", adminPwd);
			map.add("adminNm", adminNm);
			map.add("adminRrno", adminRrno);
			map.add("adminTel", adminTel);
			map.add("adminDepart", adminDepart);

			HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			System.out.println("member --- #entity# "+entity);

			/* entity를 이용한 방법 */
			ResponseEntity<RSPAUTH002> response = restTemplate.postForEntity(url, entity, RSPAUTH002.class);
			String result = response.getBody().getSuccess();

			// 응답 상태에 따른 처리
			if(result.equals("true")) {
				redirectAttributes.addFlashAttribute( "msg", "가입이 완료되었습니다." );
				redirectAttributes.addFlashAttribute( "loginRslt", "success_reg" );
				return new ModelAndView("redirect:/index");
			} else {
				redirectAttributes.addFlashAttribute( "msg", "다시 시도해 주세요." );
				redirectAttributes.addFlashAttribute( "loginRslt", "fail" );
				return new ModelAndView("redirect:/index");
			}
			
		} catch (Exception e) {
			System.out.println("API 데이터 요청 및 응답 실패");
			System.out.println("e :  "+e); 
			
		}
    	return mv;
    }
    
    /*
     * 로그아웃
     */
    @RequestMapping(value="/logout")
    public String logout(
    		HttpServletRequest request
    		) {
    	System.out.println("ㅎㅇ");
    	HttpSession session = request.getSession();
    	session.invalidate();
    	//String token = (String) session.getAttribute("token");
    	//System.out.println(token);
    	
    	return "redirect:/index";
    }

    /*
     * 비밀번호 찾기
     */
    @GetMapping("/admin_forget_pwd")
    public String admin_forget_pwd(Model model) {
    	return "login/admin_forget_pwd";
    }
    
}
