package com.pig.modules.gt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.util.*;
import com.pig.modules.gt.entity.GtMember;
import com.pig.modules.gt.mapper.GtMemberMapper;
import com.pig.modules.gt.service.GtMemberService;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.UserVO;
import com.pig.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/gtmember")
public class GtMemberController {

    @Autowired
    private GtMemberService gtMemberService;
    @Autowired
    private GtMemberMapper gtMemberMapper;

    /**
     * 详情
     */
    @GetMapping("/detail")
    public CommonResult detail(GtMember gtMember) {
        //User detail = userService.getById(user.getId());
        GtMember detail = gtMemberService.selectGtMemberById(gtMember.getId());
        return CommonResult.ok(detail);
    }

    /**
     * 用户列表
     */
    @GetMapping(value="/list",produces = "application/json;charset=utf-8")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        CommonQuery commonQuery = new CommonQuery(params);
        QueryWrapper<GtMember> queryWrapper = new QueryWrapper<GtMember>();
        if(!StringUtils.isEmpty(commonQuery.get("account"))){
            queryWrapper.like("account", commonQuery.get("account"));
        }
        if(!StringUtils.isEmpty(commonQuery.get("name"))){
            queryWrapper.like("name", commonQuery.get("name"));
        }
        queryWrapper.orderByAsc("create_time");
        Page<GtMember> pageBP = new Page<>(commonQuery.getCurrent(),commonQuery.getSize());  // 查询第1页，每页返回5条
        IPage pages = gtMemberService.page(pageBP, queryWrapper);
        return CommonResult.ok(pages);
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    public CommonResult submit(@Valid @RequestBody Map<String,Object> params) {

        CommonResult commonResult = CommonResult.failed();
        UserVo userVo = CommonUtil.getUserVoFormToken();
        params.put("createUser",userVo.getUserCode());
        params.put("createTime",new Date());

        // 用户名
        String account = StringUtil.getCheckString(params.get("account"));
        // 编号
        String code = StringUtil.getCheckString(params.get("code"));
        QueryWrapper<GtMember> queryWrapper = new QueryWrapper<GtMember>();
        queryWrapper.eq("account",account);
        GtMember gtMember = gtMemberService.getOne(queryWrapper);
        if(gtMember != null && gtMember.getAccount().equals(account)){

            commonResult.setMsg("用户名在系统中已存在，请重新输入！");
            return commonResult;
        }
        QueryWrapper<GtMember> querycodeWrapper = new QueryWrapper<GtMember>();
        querycodeWrapper.eq("code",code);
        GtMember gt2Member = gtMemberService.getOne(querycodeWrapper);
        if(gt2Member != null && gt2Member.getCode().equals(code)){

            commonResult.setMsg("编号在系统中已存在，请重新输入！");
            return commonResult;
        }
        gtMemberService.doinsert(params);

        String msg = "添加用户成功";
        return CommonResult.ok(msg);
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    public CommonResult update(@RequestBody Map<String,Object> params) {

        UserVo userVo = CommonUtil.getUserVoFormToken();
        params.put("updateUser",userVo.getUserCode());
        params.put("updateTime",new Date());

        CommonResult commonResult = CommonResult.failed();
        // 用户名
        String account = StringUtil.getCheckString(params.get("account"));
        // 编号
        String code = StringUtil.getCheckString(params.get("code"));

        QueryWrapper<GtMember> querycodeWrapper = new QueryWrapper<GtMember>();
        querycodeWrapper.eq("code",code);
        querycodeWrapper.ne("account",account);
        GtMember gt2Member = gtMemberService.getOne(querycodeWrapper);
        if(gt2Member != null && gt2Member.getCode().equals(code)){

            commonResult.setMsg("编号在系统中已存在，请重新输入！");
            return commonResult;
        }
        gtMemberMapper.doupdateById(params);
        return CommonResult.ok("修改成功");

    }


    /**
     * 修改
     */
    @PostMapping("/roleupdate")
    public CommonResult roleupdate(@RequestBody Map<String,Object> params) {

        UserVo userVo = CommonUtil.getUserVoFormToken();
        params.put("updateUser",userVo.getUserCode());
        params.put("updateTime",new Date());

        gtMemberMapper.doroleupdateById(params);
        return CommonResult.ok("修改成功");
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public CommonResult remove(@RequestBody List<String> ids) {
        for(String id : ids){
            gtMemberService.removeById(Long.parseLong(id));
        }

        return CommonResult.ok("删除成功-");
    }




    /**
     * 查询单条
     */
    @GetMapping("/info")
    public CommonResult info(GtMember gtMember) {
        QueryWrapper<GtMember> queryWrapper = new QueryWrapper<GtMember>();
        queryWrapper.like("account", gtMember.getAccount());
        GtMember one = gtMemberService.getOne(queryWrapper);
        return CommonResult.ok(one);
    }
}
