package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.dao.BizMemberDao;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.service.BizMemberService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Map;

@RestController
@RequestMapping("/wx/member")
public class MemberInfoApi {

    @Resource
    private BizMemberDao bizMemberDao;

    @Resource
    private BizMemberService bizMemberService;

    protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    /**
     * 详情
     */
    @GetMapping("/getbyaccount")
    public CommonResult getbyaccount(String openid) {

        BizMember bizMember = bizMemberDao.findByOpenidAndStatus(openid, "-1");

        return CommonResult.ok(bizMember);
    }

    /**
     * 会员信息更新操作
     */
    @PostMapping("/insertOrUpdate")
    public CommonResult insertOrUpdate(@RequestBody Map<String, Object> params) {
        return bizMemberService.insertOrUpdate(params);
    }

}
