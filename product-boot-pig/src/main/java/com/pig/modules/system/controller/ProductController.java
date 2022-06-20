package com.pig.modules.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.system.entity.Product;
import com.pig.modules.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    public CommonResult detail(Product product) {
        Product detail = productService.getById(product.getId());
        return CommonResult.ok(detail);
    }

    /**
     * 分页
     */
    @GetMapping("/list")
    public CommonResult list(@RequestParam Map<String, Object> param) {
        CommonQuery commonQuery = new CommonQuery(param);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>();
        if(!StringUtils.isEmpty(commonQuery.get("productName"))){
            queryWrapper.like("product_name", commonQuery.get("productName"));
        }
        List<Product> list = productService.list(queryWrapper);
        return CommonResult.ok(list);
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    public CommonResult submit(@RequestBody Product product) {
        productService.submit(product);
        String msg = "添加产品成功";
        if(!StringUtils.isEmpty(product.getId())){
            msg = "修改产品成功";
        }
        return CommonResult.ok(msg);
    }


    /**
     * 删除
     */
    @PostMapping("/remove")
    public CommonResult remove(@RequestParam String ids) {
        productService.removeByIds(CommonUtil.toLongList(ids));
        return CommonResult.ok();
    }

    @GetMapping("/menu-tree-keys")
    public CommonResult menuTreeKeys(String productId) {
        return productService.menuTreeKeys(productId);
    }

    @PostMapping("/menu")
    public CommonResult menu(@RequestParam String productIds,
                             @RequestParam String menuIds) {
        boolean temp = productService.productMenu(CommonUtil.toLongList(productIds), CommonUtil.toLongList(menuIds));
        if(temp){
            return CommonResult.ok("配置成功");
        }else {
            return CommonResult.failed("配置失败");
        }
    }

}

