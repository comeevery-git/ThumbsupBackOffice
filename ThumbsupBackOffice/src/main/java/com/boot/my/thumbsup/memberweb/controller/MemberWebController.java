package com.boot.my.thumbsup.memberweb.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.boot.my.thumbsup.common.RqsAPI;
import com.boot.my.thumbsup.memberweb.domain.RSPHM001;

@Controller
@RequestMapping("/member")
public class MemberWebController {
 
	@Autowired
	RqsAPI rqsApi;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	
	/*
	 * 공지사항 관리 이동
	 */
	@RequestMapping("/notice")
	@ResponseBody
	public ModelAndView notice(
			HttpServletRequest request,
			RestTemplate restTemplate) {

		HttpSession session = request.getSession();
		String token = session.getAttribute("token").toString();

	    System.out.println("BackOffice --- notice, API로 요청");

		//요청 url
		String url = "http://localhost:8007/service/webNotice";
		//요청 카테고리
		String sendCategory = "notice";
		//요청값
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("token", token);
		
		//API요청
		Map<String, Object> result_map = rqsApi.sendAPI(url, map, restTemplate, sendCategory);
		//API응답값
		ResponseEntity<RSPHM001> response = (ResponseEntity<RSPHM001>) result_map.get("response");
		//System.out.println("#응답# "+response);

		//공지사항 확인
		String result = response.getBody().getSuccess();
		System.out.println("NOTICE : " + result);
		
		
		/* 응답 상태에 따른 처리
		String result = response.getBody().getSuccess();
		// 로그인 성공
		if(result.equals("success")) {
			System.out.println("로그인 성공");
			redirectAttributes.addFlashAttribute( "msg", "환영합니다!" );
			redirectAttributes.addFlashAttribute( "loginRslt", "success_login" );
			//return new ModelAndView("redirect:/index");
			return "member/notice";
			
		// 로그인 실패
		} else {
			System.out.println("로그인 실패");
			redirectAttributes.addFlashAttribute( "msg", "아이디 또는 비밀번호를 확인해주세요." );
			redirectAttributes.addFlashAttribute( "loginRslt", "fail" );
			//return new ModelAndView("redirect:/login/admin_login");
			return "member/notice";
			
		}
			*/
		//mv.setViewName("member/notice");
		//return mv;
		ModelAndView modelAndView = new ModelAndView("redirect:member/notice");
		return modelAndView;
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * 홈페이지 관리 이동
	 */
    @GetMapping("/test123")
    public String test123(Model model) {
    	return "homepage/test123";
    }
    
    
}
