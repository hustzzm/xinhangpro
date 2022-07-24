package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.entity.BizCompany;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.service.BizCompanyService;
import com.pig.modules.gt.service.BizMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户信息
不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表控制层
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
@RestController
@RequestMapping("bizMember")
public class BizMemberController {

    /**
     * 服务对象
     */
    @Resource
    private BizMemberService bizMemberService;
    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {

        //只有普通会员、钻石会员才加入列表
        params.put("checktype","0");
        Page<BizMember> bizCompanyPage = bizMemberService.page(params);

        return CommonResult.ok(bizCompanyPage);
    }
}

