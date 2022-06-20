package com.pig.modules.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.system.entity.Param;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 参数表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/param")
public class ParamController {

    @Autowired
    private ParamService paramService;


    /**
     * 详情
     */
    @GetMapping("/detail")
    public CommonResult detail(Param param) {
        Param detail = paramService.getById(param.getId());
        return CommonResult.ok(detail);
    }

    /**
     * 分页
     */
    @GetMapping("/list")
    public CommonResult list(@RequestParam Map<String, Object> param) {
        CommonQuery commonQuery = new CommonQuery(param);
        QueryWrapper<Param> queryWrapper = new QueryWrapper<Param>();
        if(!StringUtils.isEmpty(commonQuery.get("paramName"))){
            queryWrapper.like("param_name", commonQuery.get("paramName"));
        }
        if(!StringUtils.isEmpty(commonQuery.get("paramKey"))){
            queryWrapper.like("param_key", commonQuery.get("paramKey"));
        }
        if(!StringUtils.isEmpty(commonQuery.get("paramValue"))){
            queryWrapper.like("param_value", commonQuery.get("paramValue"));
        }
        queryWrapper.orderByAsc("create_time");
        Page<Param> pageBP = new Page<>(commonQuery.getCurrent(),commonQuery.getSize());  // 查询第1页，每页返回5条
        IPage pages = paramService.page(pageBP, queryWrapper);
        return CommonResult.ok(pages);
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    public CommonResult submit(@Valid @RequestBody Param param) {
        paramService.saveOrUpdate(param);
        String msg = "添加参数成功";
        if(!StringUtils.isEmpty(param.getId())){
            msg = "修改参数成功";
        }
        return CommonResult.ok(msg);
    }


    /**
     * 删除
     */
    @PostMapping("/remove")
    public CommonResult remove(@RequestParam String ids) {
        paramService.removeByIds(CommonUtil.toLongList(ids));
        return CommonResult.ok();
    }

}

