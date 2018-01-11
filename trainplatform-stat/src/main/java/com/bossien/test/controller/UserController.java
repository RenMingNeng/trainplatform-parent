package com.bossien.test.controller;

import java.util.ArrayList;
import java.util.List;

import com.bossien.test.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class UserController {
	

	@RequestMapping("index.htm")
	public String helloUser(Model model) {
		List<User> list = new ArrayList<User>(1);
		User user =new User();
		user.setId(1);
		user.setUserName("test");
		user.setPassWord("123456");
		model.addAttribute("users", list);
		return "/user_list";
	}
	
}
