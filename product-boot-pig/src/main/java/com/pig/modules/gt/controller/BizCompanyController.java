package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.gt.dao.BizCompanyDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizCompany;
import com.pig.modules.gt.service.BizCompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 房间管理(BizCompany)表控制层
 *
 * @author makejava
 * @since 2022-07-12 23:32:30
 */
@RestController
@RequestMapping("/bizCompany")
public class BizCompanyController {
    /**
     * 服务对象
     */
    @Resource
    private BizCompanyService bizCompanyService;

    @Resource
    private BizCompanyDao bizCompanyDao;

    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        Page<BizCompany> bizCompanyPage = bizCompanyService.page(params);

        return CommonResult.ok(bizCompanyPage);
    }

    @PostMapping(value = "/save")
    public CommonResult save(@RequestBody BizCompany bizCompany) {
        if (CommonUtil.checkObjAllFieldsIsNull(bizCompany)) {
            return CommonResult.ok();
        }
        bizCompanyDao.save(bizCompany);
        return CommonResult.ok("新增成功");
    }

    @PostMapping(value = "/update")
    public CommonResult update(@RequestBody BizCompany bizCompany) {
        if (CommonUtil.checkObjAllFieldsIsNull(bizCompany)) {
            return CommonResult.ok();
        }
        bizCompanyService.update(bizCompany);
        return CommonResult.ok("修改成功");
    }

    @DeleteMapping(value = "/remove/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        bizCompanyDao.deleteById(id);
        return CommonResult.ok("删除成功");
    }
}

