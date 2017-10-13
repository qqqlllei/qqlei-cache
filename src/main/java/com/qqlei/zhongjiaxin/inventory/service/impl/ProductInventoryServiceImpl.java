package com.qqlei.zhongjiaxin.inventory.service.impl;

import javax.annotation.Resource;

import com.qqlei.zhongjiaxin.inventory.dao.RedisDAO;
import com.qqlei.zhongjiaxin.inventory.mapper.ProductInventoryMapper;
import com.qqlei.zhongjiaxin.inventory.model.ProductInventory;
import com.qqlei.zhongjiaxin.inventory.service.ProductInventoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 商品库存Service实现类
 * @author Administrator
 *
 */
@Service("productInventoryService")  
public class ProductInventoryServiceImpl implements ProductInventoryService {

	@Resource
	private ProductInventoryMapper productInventoryMapper;
	@Resource
	private RedisDAO redisDAO;
	
	@Override
	public void updateProductInventory(ProductInventory productInventory) {
		productInventoryMapper.updateProductInventory(productInventory); 
	}

	@Override
	public void removeProductInventoryCache(ProductInventory productInventory) {
		String key = "product:inventory:" + productInventory.getProductId();
		redisDAO.delete(key);
	}
	
	/**
	 * 根据商品id查询商品库存
	 * @param productId 商品id 
	 * @return 商品库存
	 */
	public ProductInventory findProductInventory(Integer productId) {
		return productInventoryMapper.findProductInventory(productId);
	}
	
	/**
	 * 设置商品库存的缓存
	 * @param productInventory 商品库存
	 */
	public void setProductInventoryCache(ProductInventory productInventory) {
		String key = "product:inventory:" + productInventory.getProductId();
		redisDAO.set(key, String.valueOf(productInventory.getInventoryCnt()));  
	}

	/**
	 * 获取商品库存的缓存
	 * @param productId
	 * @return
	 */
	@Override
	public ProductInventory getProductInventoryCache(Integer productId) {
		String key = "product:inventory:" + productId;
		String result = redisDAO.get(key);
		if(StringUtils.isBlank(result)) return null;
		return new ProductInventory(productId, Long.valueOf(result));
	}

}
