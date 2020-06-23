package com.boot.my.thumbsup.domains.Login.controller;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.boot.my.thumbsup.domains.Admin.domain.Admin;
import com.boot.my.thumbsup.domains.Admin.domain.AdminRepository;
import com.boot.my.thumbsup.domains.Admin.service.AdminService;

@Controller
@RequestMapping("/login")
public class LoginController {
 
	@Autowired
	UserDetailsService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminRepository adminRepository;
	
	/*
	 * 직원 로그인
	 */
    @GetMapping("/admin_login")
    public String main(Model model) {
    	return "login/admin_login";
    }
  
    @PostMapping(value="/userLogin")
    @ResponseBody
    public ModelAndView LoginAPI(
    		ModelAndView mv,
    			String url,
    			String id,
    			String pwd,
    			RestTemplate restTemplate,
    			RedirectAttributes redirectAttributes,
    			@RequestBody String msg
    		) {
    	String responseDtl = null;

    	System.out.println("admin --- msg@@@@@@@@@@@@@@@@@ : "+msg);
    	System.out.println("admin --- id : "+id);
    	System.out.println("admin --- pwd : "+pwd);
    	
    	System.out.println("admin --- Login, API로 요청");
    	/* API 데이터 요청 및 응답 */
		try {
			HttpHeaders headers = new HttpHeaders();
			//Charset utf8 = Charset.forName("UTF-8");
			//MediaType mediaType = new MediaType("application","json",utf8);
			//headers.setContentType(mediaType);
			System.out.println("admin --- #headers# "+headers);
	
			//요청 url
			url = "http://localhost:8007/service/auth";
			
			MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
			map.add("id", id);
			map.add("pwd", pwd);

			HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			System.out.println("admin --- #entity# "+entity);
			//ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			//response
			//응답데이터 가져오기
			responseDtl = response.getBody();
			System.out.println("----- 응답 DATA ----- "+responseDtl);
			
			// .getStatusCode = HTTP 상태코드 반환, 200:오류없이 전송성공
			// .hasBody = body유무 반환 true or false
			System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
			
			// 요청에 대한 데이터 실패값 : no_data
			if(responseDtl.equals("no_data")) {
				mv.addObject("msg","아이디 또는 비밀번호를 확인해주세요.");
				mv.setViewName("login/admin_login");
			} else {
				redirectAttributes.addFlashAttribute( "token", responseDtl );
				redirectAttributes.addFlashAttribute( "msg", "어서오세요 !" );
				return new ModelAndView("redirect:/index");
				
			}
			
		} catch (Exception e) {
			System.out.println("API 데이터 요청 및 응답 실패");
			System.out.println("e :  "+e); 
			
		}
		
    	return mv;
    	
    }
    
    
    
    
    
    
    /*
     * 직원가입
     */
    @RequestMapping(value="/admin_register")
    public ModelAndView admin_register(@ModelAttribute Admin admin,
    		HttpServletRequest request
    		) {
    	return new ModelAndView("login/admin_register");
    }
    @RequestMapping(value="/adminInsert")
    public ModelAndView adminInsert(@Validated Admin admin,
    		HttpServletRequest request,
            HttpServletResponse response
            ) {
    	//System.out.println("admin: " + admin);
    	//System.out.println("---------------------> " + request.getParameter("adminId"));

    	String encodePassword = passwordEncoder.encode(request.getParameter("adminPwd"));
    	admin.setAdminPwd(encodePassword);
    	
    	String localDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    	
    	admin.setAdminRegdate(localDate);
 	    adminRepository.save(admin);

    	//return "redirect:/login/admin_login";
    	return new ModelAndView("login/admin_login");
    }
    
    
    
    
    /*
     * 비밀번호 찾기
     */
    @GetMapping("/admin_forget_pwd")
    public String admin_forget_pwd(Model model) {
    	return "login/admin_forget_pwd";
    }
    
}
