package com.qqlei.zhongjiaxin.inventory.service;

import com.qqlei.zhongjiaxin.inventory.request.Request;

/**
 * 请求异步执行的service
 * @author 李雷
 *
 */
public interface RequestAsyncProcessService {

	void process(Request request);
}
