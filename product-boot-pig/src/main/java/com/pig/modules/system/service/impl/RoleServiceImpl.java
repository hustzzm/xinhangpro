package com.pig.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.TreeUtils;
import com.pig.modules.system.entity.DTO.TreeNode;
import com.pig.modules.system.entity.Role;
import com.pig.modules.system.entity.RoleMenu;
import com.pig.modules.system.entity.vo.MenuVO;
import com.pig.modules.system.mapper.RoleMapper;
import com.pig.modules.system.service.RoleMenuService;
import com.pig.modules.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public boolean grantMenu(List<Long> roleIds, List<Long> menus) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<RoleMenu>();
        queryWrapper.in("role_id", roleIds);
        // 删除角色配置的菜单集合
        roleMenuService.remove(queryWrapper);
        // 组装配置
        List<RoleMenu> roleMenus = new ArrayList<>();
        roleIds.forEach(roleId -> menus.forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(String.valueOf(roleId));
            roleMenu.setMenuId(String.valueOf(menuId));
            roleMenus.add(roleMenu);
        }));
        // 新增配置
        return roleMenuService.saveBatch(roleMenus);
    }

    @Override
    public CommonResult roleTree() {
        List<TreeNode> treeNodes = baseMapper.roleTree();
        Collection<TreeNode> roleVos = TreeUtils.toTree(false,treeNodes, "id", "parentId", "children", TreeNode.class);
        return CommonResult.ok(roleVos);
    }
}
