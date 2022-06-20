package com.pig.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.TreeUtils;
import com.pig.modules.system.entity.Role;
import com.pig.modules.system.entity.vo.RoleVO;
import com.pig.modules.system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public CommonResult list(@RequestParam Map<String, Object> role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        if(!StringUtils.isEmpty(role.get("roleName"))){
            queryWrapper.like("roleName", role.get("roleName"));
        }
        if(!StringUtils.isEmpty(role.get("roleAlias"))){
            queryWrapper.like("roleAlias", role.get("roleAlias"));
        }
        List<Role> roleList = roleService.list(queryWrapper);
        List<RoleVO> voList = new ArrayList<>();
        roleList.forEach(roleEntity -> {
            RoleVO vo = new RoleVO();
            BeanUtils.copyProperties(roleEntity,vo);
            voList.add(vo);
        });
        /**
         * 查询的时候不一定有root用户
         */
        List<Role> collect = voList.stream().filter(roleVO -> roleVO.getParentId().equals("0")).collect(Collectors.toList());
        Collection<RoleVO> roleVOS = null;
        if(collect.size()>0){
            roleVOS = TreeUtils.toTree(false,voList, "id", "parentId", "children", RoleVO.class);
            return CommonResult.ok(roleVOS);
        }
        return CommonResult.ok(roleList);
    }

    /**
     * 列表
     */
    @GetMapping("/listall")
    public CommonResult listall(@RequestParam Map<String, Object> role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        if(!StringUtils.isEmpty(role.get("roleName"))){
            queryWrapper.like("roleName", role.get("roleName"));
        }
        if(!StringUtils.isEmpty(role.get("roleAlias"))){
            queryWrapper.like("roleAlias", role.get("roleAlias"));
        }
        List<Role> roleList = roleService.list(queryWrapper);

        return CommonResult.ok(roleList);
    }

    /**
     * 设置菜单权限
     *
     * @param roleIds
     * @param menuIds
     * @return
     */
    @PostMapping("/grant")
    public CommonResult grant(@RequestParam String roleIds,
                   @RequestParam String menuIds) {
        boolean temp = roleService.grantMenu(CommonUtil.toLongList(roleIds), CommonUtil.toLongList(menuIds));
        if(temp){
            return CommonResult.ok("授权成功");
        }else {
            return CommonResult.failed("授权失败");
        }
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public CommonResult remove(@RequestParam String ids) {
        roleService.removeByIds(CommonUtil.toLongList(ids));
        return CommonResult.ok("删除成功");
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    public CommonResult<Role> detail(Role role) {
        Role detail = roleService.getById(role.getId());
        return CommonResult.ok(detail);
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    public CommonResult submit(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        String msg = "添加角色成功";
        if(!StringUtils.isEmpty(role.getId())){
            msg = "修改角色成功";
        }
        return CommonResult.ok(msg);
    }

    /**
     * 获取角色树形结构
     */
    @GetMapping("/tree")
    public CommonResult tree() {
        return roleService.roleTree();
    }
}
