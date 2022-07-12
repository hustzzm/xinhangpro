package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.BizCompanyDao;
import com.pig.modules.gt.dao.BizMemberDao;
import com.pig.modules.gt.entity.BizCompany;
import com.pig.modules.gt.entity.BizMember;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/company")
public class CompanyApi {

    /***
     * 公司信息查询api
     */
    @Resource
    private BizCompanyDao bizCompanyDao;

    protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    /**
     * 详情
     */
    @GetMapping("/getcompany")
    public CommonResult getcompany() {

        List<BizCompany> list = bizCompanyDao.findAll();

        return CommonResult.ok(list);
    }

    /**
     * 详情
     */
    @GetMapping("/getcompanybyid")
    public CommonResult getcompanybyid(String id) {

        BizCompany bizCompany = bizCompanyDao.getBizCompanyById(Integer.parseInt(id));

        return CommonResult.ok(bizCompany);
    }

}
