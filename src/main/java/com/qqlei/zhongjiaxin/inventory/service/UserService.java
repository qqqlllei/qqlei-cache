package com.qqlei.zhongjiaxin.inventory.service;

import com.qqlei.zhongjiaxin.inventory.model.User;

public interface UserService {

	public User findUserInfo();

	public User getCachedUserInfo();
	
}
