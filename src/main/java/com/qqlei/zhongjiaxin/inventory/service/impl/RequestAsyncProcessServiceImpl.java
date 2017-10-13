package com.qqlei.zhongjiaxin.inventory.service.impl;
import com.qqlei.zhongjiaxin.inventory.request.ProductInventoryCacheRefreshRequest;
import com.qqlei.zhongjiaxin.inventory.request.ProductInventoryDBUpdateRequest;
import com.qqlei.zhongjiaxin.inventory.request.Request;
import com.qqlei.zhongjiaxin.inventory.request.RequestQueue;
import com.qqlei.zhongjiaxin.inventory.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 请求异步处理的service实现
 * @author Administrator
 *
 */
@Service("requestAsyncProcessService")  
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {
	
	@Override
	public void process(Request request) {
		try {

			//读请求去重
			RequestQueue requestQueue = RequestQueue.getInstance();
			Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

			// 更新数据库的请求，那么就将那个productId对应的标识设置为true
			if(request instanceof ProductInventoryDBUpdateRequest) flagMap.put(request.getProductId(), true);

			// 读请求
			if(request instanceof ProductInventoryCacheRefreshRequest) {
				Boolean flag = flagMap.get(request.getProductId());

				if(flag == null) flagMap.put(request.getProductId(), false);

				// 标志位为true ，证明前面是更新请求
				if(flag != null && flag) flagMap.put(request.getProductId(), false);

				// 如果是缓存刷新的请求，而且发现标识不为空，但是标识是false
				// 前面是一个读请求
				if(flag != null && !flag) return;
			}


			// 做请求的路由，根据每个请求的商品id，路由到对应的内存队列中去
			ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());
			// 将请求放入对应的队列中，完成路由操作
			queue.put(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取路由到的内存队列
	 * @param productId 商品id
	 * @return 内存队列
	 */
	private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
		RequestQueue requestQueue = RequestQueue.getInstance();
		
		// 先获取productId的hash值
		String key = String.valueOf(productId);
		int h;
		int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
		
		// 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
		// 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
		// 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
		int index = (requestQueue.queueSize() - 1) & hash;
		System.out.println("=======================: 路由内存队列，商品id=" + productId + ", 队列索引=" + index);
		return requestQueue.getQueue(index);
	}

}
