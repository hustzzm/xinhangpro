package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.service.BizBookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * (BizBooking)表控制层
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
@RestController
@RequestMapping("/booking")
public class BizBookingController {
    /**
     * 服务对象
     */
    @Resource
    private BizBookingService bizBookingService;

    @Resource
    private BizBookingDao bizBookingDao;

    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        Page<BizBooking> bookingPage = bizBookingService.page(params);

        return CommonResult.ok(bookingPage);
    }

    @PutMapping(value = "/finished/{id}")
    public CommonResult finished(@PathVariable Integer id) {
        bizBookingDao.updateBookStatusById(id);
        return CommonResult.ok("预约已完成！");
    }

    @DeleteMapping(value = "/remove/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        bizBookingDao.deleteById(id);
        return CommonResult.ok("预约已删除！");
    }
}

