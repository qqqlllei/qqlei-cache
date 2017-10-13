package com.qqlei.zhongjiaxin.inventory.controller;

import com.qqlei.zhongjiaxin.inventory.model.User;
import com.qqlei.zhongjiaxin.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 用户Controller控制器
 * @author Administrator
 *
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/getUserInfo")
	@ResponseBody
	public User getUserInfo() {
		User user = userService.findUserInfo();
		return user;
	}
	
	@RequestMapping("/getCachedUserInfo")
	@ResponseBody
	public User getCachedUserInfo() {
		User user = userService.getCachedUserInfo();
		return user;
	}
	
}
