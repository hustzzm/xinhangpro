package com.pig.modules.system.mapper;

import com.pig.modules.system.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
public interface ProductMapper extends BaseMapper<Product> {

    List<Product> getUserProduct(List<Long> roleId);
}
