package com.qqlei.zhongjiaxin.inventory.request;

import com.qqlei.zhongjiaxin.inventory.model.ProductInventory;
import com.qqlei.zhongjiaxin.inventory.service.ProductInventoryService;

/**
 * 重新加载商品库存的缓存
 * Created by 李雷 on 2017/10/12.
 */
public class ProductInventoryCacheRefreshRequest implements Request {

    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 商品库存Service
     */
    private ProductInventoryService productInventoryService;

    public ProductInventoryCacheRefreshRequest(Integer productId,ProductInventoryService productInventoryService) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
    }
    @Override
    public void process() {
        // 从数据库中查询最新的商品库存数量
        ProductInventory productInventory = productInventoryService.findProductInventory(productId);
        System.out.println("=======================读请求"+Thread.currentThread().getName()+"从DB中获取数据==========================InventoryCnt"+productInventory.getInventoryCnt());
        // 将最新的商品库存数量，刷新到redis缓存中去
        productInventoryService.setProductInventoryCache(productInventory);
    }

    @Override
    public Integer getProductId() {
        return productId;
    }
}
