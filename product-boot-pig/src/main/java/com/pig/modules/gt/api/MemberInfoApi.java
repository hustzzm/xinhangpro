package com.pig.modules.gt.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.service.BizMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/member")
public class MemberInfoApi {

    @Autowired
    private BizMemberService bizMemberService;

    /**
     * 详情
     */
    @GetMapping("/getbyaccount")
    public CommonResult getbyaccount(String openid) {

        QueryWrapper<BizMember> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("openid", openid);
        queryWrapper.eq("status", -1);

        //User detail = userService.getById(user.getId());
        BizMember member = bizMemberService.getOne(queryWrapper);

        return CommonResult.ok(member);
    }


}
