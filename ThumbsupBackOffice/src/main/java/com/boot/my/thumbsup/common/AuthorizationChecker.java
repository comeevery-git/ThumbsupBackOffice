package com.boot.my.thumbsup.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationChecker {

	//API서버로 로그인 요청 후 Jwt토큰/권한으로 제어
	public boolean check(HttpServletRequest request, Authentication authentication) {
		HttpSession session = request.getSession();
		
        if(session.getAttribute("role")!=null) {
        	String role = session.getAttribute("role").toString();
        	System.out.println(session.getAttribute("role"));
        	
	        if(role.equals("[ROLE_ADMIN]")) {
	        	System.out.println("ADMIN 권한입니다.");
	        	return true;
	        }

        } else {
        	System.out.println("권한이 없습니다.");
        	return false;
        	
        }
        
        return false;
    }



}
