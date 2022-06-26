package com.pig.modules.system.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.TreeUtils;
import com.pig.modules.system.entity.DTO.MenuDTO;
import com.pig.modules.system.entity.DTO.TreeNode;
import com.pig.modules.system.entity.Menu;
import com.pig.modules.system.entity.RoleMenu;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.MenuVO;
import com.pig.modules.system.mapper.MenuMapper;
import com.pig.modules.system.mapper.RoleMenuMapper;
import com.pig.modules.system.service.MenuService;
import com.pig.modules.system.service.RoleMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public CommonResult getMenuForRoleId(String roleId) {
        List<Menu> menus = baseMapper.roleMenu(CommonUtil.toLongList(roleId));
        List<MenuVO> voList = new ArrayList<>();
        menus.forEach(menu -> {
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(menu,vo);
            voList.add(vo);
        });
        Collection<MenuVO> menuVOS = TreeUtils.toTree(false,voList, "id", "parentId", "children", MenuVO.class);
        return CommonResult.ok(menuVOS);
    }

    @Override
    public CommonResult getMenuForRoleIdAndProductId(String roleId, String productId) {
        List<Menu> menus = baseMapper.roleMenuAndProduct(CommonUtil.toLongList(roleId),productId);
        List<MenuVO> voList = new ArrayList<>();
        menus.forEach(menu -> {
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(menu,vo);
            voList.add(vo);
        });
        Collection<MenuVO> menuVOS = TreeUtils.toTree(false,voList, "id", "parentId", "children", MenuVO.class);
        return CommonResult.ok(menuVOS);
    }

    @Override
    public CommonResult getButtonsForRoleId(String roleId) {
        List<Menu> buttons = baseMapper.getButtons(CommonUtil.toLongList(roleId));
        List<MenuVO> voList = new ArrayList<>();
        buttons.forEach(menu -> {
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(menu,vo);
            voList.add(vo);
        });
        List<MenuVO> roots = voList.stream().filter(menuVO -> menuVO.getCategory() == 1).collect(Collectors.toList());
        Collection<MenuVO> menuVOS = TreeUtils.toTreeHaveRoot(roots,voList, "id", "parentId", "children", MenuVO.class);
        return CommonResult.ok(menuVOS);
    }

    @Override
    public CommonResult getButtonsForRoleIdAndProductId(String roleId, String productId) {
        List<Menu> buttons = baseMapper.getButtonsByProduct(CommonUtil.toLongList(roleId),productId);
        List<MenuVO> voList = new ArrayList<>();
        buttons.forEach(menu -> {
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(menu,vo);
            voList.add(vo);
        });
        List<MenuVO> roots = voList.stream().filter(menuVO -> menuVO.getCategory() == 1).collect(Collectors.toList());
        Collection<MenuVO> menuVOS = TreeUtils.toTreeHaveRoot(roots,voList, "id", "parentId", "children", MenuVO.class);
        return CommonResult.ok(menuVOS);
    }

    @Override
    public CommonResult getAuthRoles(String roleId) {
        List<MenuDTO> authRoutes = baseMapper.getAuthRoutes(CommonUtil.toLongList(roleId));
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//        authRoutes.forEach(route -> list.add(new HashMap<>().put(route.getPath(), new HashMap<>().put("authority", route.getAlias().split(","))));
        authRoutes.forEach(route -> {
            Map<String,Object> map = new HashMap<>();
            map.put(route.getPath(),new HashMap<>().put("authority", route.getAlias().split(",")));
            list.add(map);
        });
        return CommonResult.ok(list);
    }

    @Override
    public CommonResult getMenuTree() {
        List<TreeNode> tree = baseMapper.getTree();
        Collection<TreeNode> menuVOS = TreeUtils.toTree(false,tree, "id", "parentId", "children", TreeNode.class);
        return CommonResult.ok(menuVOS);
    }

    @Override
    public CommonResult grantTree() {
        List<TreeNode> tree = baseMapper.grantTree();
        Collection<TreeNode> menuVOS = TreeUtils.toTree(false,tree, "id", "parentId", "children", TreeNode.class);
        return CommonResult.ok(menuVOS);
    }

    @Override
    public CommonResult roleTreeKeys(String roleIds) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<RoleMenu>();
        queryWrapper.in("role_id",CommonUtil.toLongList(roleIds));
        List<RoleMenu> roleMenus = roleMenuService.list(queryWrapper);
        List<String> menuIdList = roleMenus.stream().map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
        return CommonResult.ok(menuIdList);
    }
}
