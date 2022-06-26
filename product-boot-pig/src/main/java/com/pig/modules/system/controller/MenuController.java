package com.pig.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.TreeUtils;
import com.pig.modules.system.entity.Menu;
import com.pig.modules.system.entity.vo.MenuVO;
import com.pig.modules.system.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    /**
     * 前端菜单数据
     */
    @GetMapping("/routes")
    public CommonResult<List<MenuVO>> routes(UserVo user) {
        return menuService.getMenuForRoleId(user.getRoleId());
    }

    /**
     * 前端按钮数据
     */
    @GetMapping("/buttons")
    public CommonResult<List<MenuVO>> buttons(UserVo user) {
//        List<MenuVO> list = menuService.buttons(user.getRoleId());
//        return R.data(list);
        return menuService.getButtonsForRoleId(user.getRoleId());
    }

    /**
     * 前端菜单数据
     */
    @GetMapping("/routesByProduct")
    public CommonResult<List<MenuVO>> dynamicRoutesByProduct(@RequestParam String id,UserVo user) {
        return menuService.getMenuForRoleIdAndProductId(user.getRoleId(),id);
    }

    /**
     * 前端按钮数据
     */
    @GetMapping("/buttonsByProduct")
    public CommonResult<List<MenuVO>> buttonsByProduct(@RequestParam String id,UserVo user) {
//        List<MenuVO> list = menuService.buttons(user.getRoleId());
//        return R.data(list);
        return menuService.getButtonsForRoleIdAndProductId(user.getRoleId(),id);
    }

    /**
     * 获取配置的角色权限
     */
    @GetMapping("authRoutes")
    public CommonResult authRoutes(UserVo user) {
        if (user==null) {
            return null;
        }
        return menuService.getAuthRoles(user.getRoleId());
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public CommonResult list(@RequestParam Map<String, Object> menuParams) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        if(!StringUtils.isEmpty(menuParams.get("code"))){
            queryWrapper.like("code", menuParams.get("code"));
        }
        if(!StringUtils.isEmpty(menuParams.get("name"))){
            queryWrapper.like("name", menuParams.get("name"));
        }
        queryWrapper.orderByAsc("sort");
        List<Menu> menuList = menuService.list(queryWrapper);
        List<MenuVO> voList = new ArrayList<>();
        menuList.forEach(menu -> {
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(menu,vo);
            voList.add(vo);
        });
        /**
         * 查询的时候不一定有root菜单
         */
        List<MenuVO> collect = voList.stream().filter(menuVO -> menuVO.getParentId().equals("0")).collect(Collectors.toList());
        Collection<MenuVO> menuVOS = null;
        if(collect.size()>0){
            menuVOS = TreeUtils.toTree(false,voList, "id", "parentId", "children", MenuVO.class);
        }else{
            List<MenuVO> rootList = voList.stream().filter(menuVO -> menuVO.getCategory() == 1).collect(Collectors.toList());
            menuVOS = TreeUtils.toTreeHaveRoot(rootList,voList, "id", "parentId", "children", MenuVO.class);
        }
        return CommonResult.ok(menuVOS);
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    public CommonResult<Menu> detail(Menu menu) {
        Menu byId = menuService.getById(menu.getId());
        return CommonResult.ok(byId);
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public CommonResult remove(@RequestParam String ids) {
        menuService.removeByIds(CommonUtil.toLongList(ids));
        return CommonResult.ok("删除成功");
    }

    /**
     * 获取菜单树形结构
     */
    @GetMapping("/tree")
    public CommonResult tree() {
        return menuService.getMenuTree();
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    public CommonResult submit(@Valid @RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        String msg = "添加菜单成功";
        if(!StringUtils.isEmpty(menu.getId())){
            msg = "修改菜单成功";
        }
        return CommonResult.ok(msg);
    }

    /**
     * 获取权限分配树形结构
     */
    @GetMapping("/grant-tree")
    public CommonResult grantTree() {
        return menuService.grantTree();
    }

    /**
     * 获取权限分配树形结构
     */
    @GetMapping("/role-tree-keys")
    public CommonResult roleTreeKeys(String roleIds) {
        return menuService.roleTreeKeys(roleIds);
    }


}
