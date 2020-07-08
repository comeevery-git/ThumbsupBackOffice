package com.boot.my.thumbsup.domains.Admin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boot.my.thumbsup.domains.Admin.domain.AdminRepository;
import com.boot.my.thumbsup.domains.Admin.service.AdminService;
import com.boot.my.thumbsup.domains.Board.domain.BoardEntity;
import com.boot.my.thumbsup.domains.Board.domain.BoardRepository;
import com.boot.my.thumbsup.domains.Board.service.BoardService;

@Controller
public class AdminController {
 
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	public BoardService boardService;
	@Autowired
	public BoardRepository boardRepository;
	;
	
    /*
     * 관리자페이지 index
     */
    @RequestMapping("/index")
    public String index(
    		Model model) {
    	boardRepository.deleteAll();
    	boardService.save(new BoardEntity("Ramesh", "Fadatare", "ramesh@gmail.com"));
    	boardService.save(new BoardEntity("Tom", "Cruise", "tom@gmail.com"));
    	boardService.save(new BoardEntity("John", "Cena", "john@gmail.com"));
    	boardService.save(new BoardEntity("tony", "stark", "stark@gmail.com"));


        //List <BoardEntity> boards = boardService.findAll();
        //boards.forEach(employee -> System.out.println(employee.toString()));
        
    	Iterable <BoardEntity> boardList = boardRepository.findAll();
    			
			for(BoardEntity i : boardList ){
				System.out.println(i.toString());
			}

        model.addAttribute("board_list",boardRepository.findAll());
    	model.addAttribute("test","test");
    	return "index";
    }
    
    
}
