package com.pig.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.system.entity.Product;
import com.pig.modules.system.entity.ProductMenu;
import com.pig.modules.system.mapper.ProductMapper;
import com.pig.modules.system.service.ProductMenuService;
import com.pig.modules.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMenuService productMenuService;

    @Override
    public boolean submit(Product product) {
        LambdaQueryWrapper<Product> lqw = Wrappers.<Product>query().lambda().eq(Product::getProductCode, product.getProductCode());
        Integer cnt = baseMapper.selectCount((StringUtils.isEmpty(product.getId())) ? lqw : lqw.notIn(Product::getId, product.getId()));
        if (cnt > 0) {
            throw new ApiException("当前产品编码已存在!");
        }
        return saveOrUpdate(product);
    }

    @Override
    public CommonResult menuTreeKeys(String productId) {
        QueryWrapper<ProductMenu> queryWrapper = new QueryWrapper<ProductMenu>();
        queryWrapper.in("product_id", CommonUtil.toLongList(productId));
        List<ProductMenu> productMenuList = productMenuService.list(queryWrapper);
        List<String> menuIdList = productMenuList.stream().map(productMenu -> productMenu.getMenuId()).collect(Collectors.toList());
        return CommonResult.ok(menuIdList);
    }

    @Override
    public boolean productMenu(List<Long> productIds, List<Long> menus) {
        QueryWrapper<ProductMenu> queryWrapper = new QueryWrapper<ProductMenu>();
        queryWrapper.in("product_id", productIds);
        // 删除角色配置的菜单集合
        productMenuService.remove(queryWrapper);
        // 组装配置
        List<ProductMenu> productMenuList = new ArrayList<>();
        productIds.forEach(productId -> menus.forEach(menuId -> {
            ProductMenu productMenu = new ProductMenu();
            productMenu.setProductId(String.valueOf(productId));
            productMenu.setMenuId(String.valueOf(menuId));
            productMenuList.add(productMenu);
        }));
        // 新增配置
        return productMenuService.saveBatch(productMenuList);
    }

    @Override
    public List<Product> getUserProduct(List<Long> roleId) {
        return baseMapper.getUserProduct(roleId);
    }
}
