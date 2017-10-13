package com.qqlei.zhongjiaxin.inventory.controller;

import javax.annotation.Resource;

import com.qqlei.zhongjiaxin.inventory.model.ProductInventory;
import com.qqlei.zhongjiaxin.inventory.request.ProductInventoryCacheRefreshRequest;
import com.qqlei.zhongjiaxin.inventory.request.ProductInventoryDBUpdateRequest;
import com.qqlei.zhongjiaxin.inventory.request.Request;
import com.qqlei.zhongjiaxin.inventory.service.ProductInventoryService;
import com.qqlei.zhongjiaxin.inventory.service.RequestAsyncProcessService;
import com.qqlei.zhongjiaxin.inventory.vo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品库存Controller
 * @author 李雷
 *
 */
@Controller
public class ProductInventoryController {

	@Resource
	private RequestAsyncProcessService requestAsyncProcessService;
	@Resource
	private ProductInventoryService productInventoryService;
	
	/**
	 * 更新商品库存
	 */
	@RequestMapping("/updateProductInventory")
	@ResponseBody
	public Response updateProductInventory(ProductInventory productInventory) {
		Response response ;
		
		try {
			Request request = new ProductInventoryDBUpdateRequest(
					productInventory, productInventoryService);
			requestAsyncProcessService.process(request);
			response = new Response(Response.SUCCESS,"更新的id："+productInventory.getProductId()+"更新的库存："+productInventory.getInventoryCnt());
		} catch (Exception e) {
			e.printStackTrace();
			response = new Response(Response.FAILURE);
		}
		
		return response;
	}
	
	/**
	 * 获取商品库存
	 */
	@RequestMapping("/getProductInventory")
	@ResponseBody
	public ProductInventory getProductInventory(Integer productId) {
		ProductInventory productInventory ;
		
		try {
			Request request = new ProductInventoryCacheRefreshRequest(
					productId, productInventoryService);
			requestAsyncProcessService.process(request);
			
			// 将请求扔给service异步去处理以后，就需要while(true)一会儿
			// 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
			long startTime = System.currentTimeMillis();
			long endTime;
			long waitTime = 0L;
			
			// 等待超过200ms没有从缓存中获取到结果
			while(true) {
				if(waitTime > 200) {
					break;
				}
				
				// 尝试去redis中读取一次商品库存的缓存数据
				productInventory = productInventoryService.getProductInventoryCache(productId);
				
				// 如果读取到了结果，那么就返回
				if(productInventory != null) {
					System.out.println("=======================读请求"+Thread.currentThread().getName()+"从缓存中获取数据==========================InventoryCnt"+productInventory.getInventoryCnt());
					return productInventory;
				}
				
				// 如果没有读取到结果，那么等待一段时间
				else {
					System.out.println("=======================读请求"+Thread.currentThread().getName()+"从缓存中没有获取数据，等待==========================");
					Thread.sleep(20);
					endTime = System.currentTimeMillis();
					waitTime = endTime - startTime;
				}
			}

			/**
			 * 当写请求过来，删除了redis的数据并且更新时间
			 */
			// 直接尝试从数据库中读取数据
			productInventory = productInventoryService.findProductInventory(productId);
			if(productInventory != null) {

				System.out.println("=======================读请求"+Thread.currentThread().getName()+"从DB中获取数据并且刷新缓存，==========================InventoryCnt"+productInventory.getInventoryCnt());
				// 将缓存刷新一下,避免大量请求直接落库
				productInventoryService.setProductInventoryCache(productInventory);
				return productInventory;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ProductInventory(productId, -1L);  
	}
	
}
