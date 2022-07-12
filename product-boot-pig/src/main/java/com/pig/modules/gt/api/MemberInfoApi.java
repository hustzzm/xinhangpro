package com.pig.modules.gt.api;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.BizMemberDao;
import com.pig.modules.gt.entity.BizMember;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/member")
public class MemberInfoApi {

    @Resource
    private BizMemberDao bizMemberDao;

    protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    /**
     * 详情
     */
    @GetMapping("/getbyaccount")
    public CommonResult getbyaccount(String openid) {

        BizMember bizMember = bizMemberDao.findByOpenidAndStatus(openid, -1);

        return CommonResult.ok(bizMember);
    }

    /**
     * 会员信息更新操作
     */
    @PostMapping ("/update")
    public CommonResult update(@RequestBody Map<String,Object> params) {


        BizMember bizMember = new BizMember();
        bizMember.setId(StringUtil.getCheckString(params.get("id")));
        if(!StringUtil.isNull(params.get("pigjs1"))){
            bizMember.setPigjs1(StringUtil.getCheckString(params.get("pigjs1")));
        }
        if(!StringUtil.isNull(params.get("pigjs2"))){
            bizMember.setPigjs1(StringUtil.getCheckString(params.get("pigjs2")));
        }
        if(!StringUtil.isNull(params.get("pigxs1"))){
            bizMember.setPigxs1(StringUtil.getCheckString(params.get("pigxs1")));
        }
        if(!StringUtil.isNull(params.get("pigxs2"))){
            bizMember.setPigxs2(StringUtil.getCheckString(params.get("pigxs2")));
        }
        if(!StringUtil.isNull(params.get("pigsf1"))){
            bizMember.setPigsf1(StringUtil.getCheckString(params.get("pigsf1")));
        }
        if(!StringUtil.isNull(params.get("pigsf2"))){
            bizMember.setPigsf2(StringUtil.getCheckString(params.get("pigsf2")));
        }

        bizMember.setUpdatedBy(StringUtil.getCheckString(params.get("nickname")));
        bizMember.setUpdateTime(new Date());
        bizMemberDao.save(bizMember);

        return CommonResult.ok(bizMember);
    }

}
