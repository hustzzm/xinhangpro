package com.pig.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.basic.util.CommonResult;
import com.pig.modules.system.entity.Menu;
import com.pig.modules.system.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface RoleService extends IService<Role> {

    /**
     * 配置授权菜单
     * @param roleIds
     * @param menus
     * @return
     */
    boolean grantMenu(List<Long> roleIds, List<Long> menus);

    /**
     * 获取权限分配树形结构
     * @return
     */
    CommonResult roleTree();

}
