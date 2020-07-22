package com.boot.my.thumbsup.domains.Admin.domain;

import com.boot.my.thumbsup.domains.login.domain.RqsInterData;

import lombok.Getter;
import lombok.Setter;

/*
 * 홈페이지 공지사항
 */

@Getter
@Setter
public class RSPHM001 implements RqsInterData{
	// 결과값
	private String success;
	
	// 결과msg
	private String msg;


    
}
