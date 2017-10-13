package com.qqlei.zhongjiaxin.inventory.request;

import com.qqlei.zhongjiaxin.inventory.model.ProductInventory;
import com.qqlei.zhongjiaxin.inventory.service.ProductInventoryService;

/**
 *
 * 数据更新请求
 * Created by 李雷 on 2017/10/12.
 *
 *
 * （1）删除缓存
 * （2）更新数据库
 */
public class ProductInventoryDBUpdateRequest implements Request {

    /**
     * 商品库存
     */
    private ProductInventory productInventory;
    /**
     * 商品库存Service
     */
    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory,
                                           ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        // 删除redis中的缓存
        productInventoryService.removeProductInventoryCache(productInventory);
        System.out.println("=======================写请求"+Thread.currentThread().getName()+"删除redis中的缓存==========================");


        try {
            Thread.sleep(20000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 修改数据库中的库存
        productInventoryService.updateProductInventory(productInventory);
        System.out.println("=======================写请求"+Thread.currentThread().getName()+"修改数据库中的库存==========================InventoryCnt"+productInventory.getInventoryCnt());
    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }
}
