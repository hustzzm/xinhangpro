package com.pig.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.basic.util.CommonResult;
import com.pig.modules.system.entity.Dept;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface DeptService extends IService<Dept> {

    /**
     * 获取部门树
     * @return
     */
    CommonResult deptTree();

}
