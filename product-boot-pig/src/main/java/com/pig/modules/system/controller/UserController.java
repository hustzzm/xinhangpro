package com.pig.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.UserVO;
import com.pig.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    public CommonResult detail(User user) {
        //User detail = userService.getById(user.getId());
        UserVO detail = userService.selectUserVoById(user.getId());
        return CommonResult.ok(detail);
    }

    /**
     * 用户列表
     */
    @GetMapping("/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        CommonQuery commonQuery = new CommonQuery(params);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        if(!StringUtils.isEmpty(commonQuery.get("account"))){
            queryWrapper.like("account", commonQuery.get("account"));
        }
        if(!StringUtils.isEmpty(commonQuery.get("name"))){
            queryWrapper.like("name", commonQuery.get("name"));
        }
        queryWrapper.orderByAsc("create_time");
        Page<User> pageBP = new Page<>(commonQuery.getCurrent(),commonQuery.getSize());  // 查询第1页，每页返回5条
        IPage pages = userService.page(pageBP, queryWrapper);
        return CommonResult.ok(pages);
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    public CommonResult submit(@Valid @RequestBody User user) {
        userService.submit(user);
        String msg = "添加用户成功";
        if(!StringUtils.isEmpty(user.getId())){
            msg = "修改用户成功";
        }
        return CommonResult.ok(msg);
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    public CommonResult update(@RequestBody User user) {
        userService.updateById(user);
        return CommonResult.ok("修改成功");
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public CommonResult remove(@RequestParam String ids) {
        userService.removeByIds(CommonUtil.toLongList(ids));
        return CommonResult.ok("删除成功-");
    }

    /**
     * 设置菜单权限
     *
     * @param userIds
     * @param roleIds
     * @return
     */
    @PostMapping("/grantRole")
    public CommonResult grant(@RequestParam String userIds,
                   @RequestParam String roleIds) {
        boolean temp = userService.grantRole(userIds, roleIds);
        return CommonResult.ok();
    }

    @PostMapping("/reset-password")
    public CommonResult resetPassword(@RequestParam String userIds) {
        boolean temp = userService.resetPassword(userIds);
        return CommonResult.ok("重置密码成功");
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param newPassword1
     * @return
     */
    @PostMapping("/update-password")
    public CommonResult updatePassword(UserVo user, @RequestParam String oldPassword,
                            @RequestParam String newPassword,
                            @RequestParam String newPassword1) {
        boolean temp = userService.updatePassword(user.getUserCode(), oldPassword, newPassword, newPassword1);
        if(temp){
            return CommonResult.ok("密码修改成功!");
        }else{
            return CommonResult.ok("密码修改失败!");
        }

    }

    /**
     * 查询单条
     */
    @GetMapping("/info")
    public CommonResult info(UserVo userVO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("account", userVO.getUserCode());
        User one = userService.getOne(queryWrapper);
        return CommonResult.ok(one);
    }
}
