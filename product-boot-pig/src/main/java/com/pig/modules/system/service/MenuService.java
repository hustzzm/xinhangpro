package com.pig.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.basic.util.CommonResult;
import com.pig.modules.system.entity.Menu;
import com.pig.modules.system.entity.vo.MenuVO;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface MenuService extends IService<Menu> {

    /**
     * 根据角色获取菜单
     * @param roleId
     * @return
     */
    CommonResult getMenuForRoleId(String roleId);

    /**
     * 根据角色产品获取菜单
     * @param roleId
     * @return
     */
    CommonResult getMenuForRoleIdAndProductId(String roleId,String productId);

    /**
     * 根据角色获取按钮权限
     * @param roleId
     * @return
     */
    CommonResult getButtonsForRoleId(String roleId);

    /**
     * 根据角色和产品获取按钮权限
     * @param roleId
     * @return
     */
    CommonResult getButtonsForRoleIdAndProductId(String roleId,String productId);

    /**
     * 获取授权的菜单
     * @param roleId
     * @return
     */
    CommonResult getAuthRoles(String roleId);

    /**
     * 树形结构
     *
     * @return
     */
    CommonResult getMenuTree();

    /**
     * 获取权限分配树形结构
     * @return
     */
    CommonResult grantTree();

    /**
     * 根据角色id获取已经分配的权限
     * @param roleIds
     */
    CommonResult roleTreeKeys(String roleIds);

}
