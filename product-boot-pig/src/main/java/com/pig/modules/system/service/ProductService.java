package com.pig.modules.system.service;

import com.pig.basic.util.CommonResult;
import com.pig.modules.system.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.modules.system.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
public interface ProductService extends IService<Product> {

    /**
     * 添加/修改用户
     * @param product
     * @return
     */
    boolean submit(Product product);

    /**
     * 获取产品配置的菜单树
     * @param productId
     * @return
     */
    CommonResult menuTreeKeys(String productId);

    /**
     * 配置授权菜单
     * @param productIds
     * @param menus
     * @return
     */
    boolean productMenu(List<Long> productIds, List<Long> menus);

    /**
     * 根据用户角色获取产品信息
     * @param roleId
     * @return
     */
    List<Product> getUserProduct(List<Long> roleId);
}
