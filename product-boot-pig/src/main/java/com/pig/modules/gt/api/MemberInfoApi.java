package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.DateUtil;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.BizMemberDao;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.service.BizMemberService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/wx/member")
public class MemberInfoApi {

    @Resource
    private BizMemberDao bizMemberDao;

    @Resource
    private BizMemberService bizMemberService;

    protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

    /**
     * 详情
     */
    @GetMapping("/getbyaccount")
    public CommonResult getbyaccount(String openid) {

        BizMember bizMember = bizMemberDao.findByOpenidAndStatus(openid, "-1");
        if(bizMember ==null){
            return CommonResult.failed();
        }
        return CommonResult.ok(bizMember);
    }

    /**
     * 会员信息更新操作
     */
    @PostMapping("/insertOrUpdate")
    public CommonResult insertOrUpdate(@RequestBody Map<String, Object> params) {
        return bizMemberService.insertOrUpdate(params);
    }

    /**
     * 在用户进行升级时，获取剩余天数
     */
    @GetMapping("/getRestDayCount")
    public CommonResult getRestDayCount(String openid) throws ParseException {

        //查询唯一的一条会员信息
        BizMember bizMember = bizMemberDao.findByOpenidAndStatus(openid, "-1");
        if(bizMember ==null || StringUtil.isNull(bizMember.getEndDate())){
            return CommonResult.failed("会员记录为空");
        }

        //当前时间
        Date dateNow = new Date();

        Date dateEnd = sdf.parse(bizMember.getEndDate());

        int idayCount = DateUtil.daysOfTwo(dateNow,dateEnd);
        if(idayCount <= 0){
            return CommonResult.failed("会员已过期，不可进行升级操作");
        }

        return CommonResult.ok(String.valueOf(idayCount));
    }

}
