package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizRoomManage;
import com.pig.modules.gt.service.BizBookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/list")
    public CommonResult test(@RequestParam Map<String, Object> params) {
        Page<BizBooking> bookingPage = bizBookingService.page(params);
        if (!ObjectUtils.isEmpty(bookingPage.getContent())) {
            List<BizBooking> content = bookingPage.getContent();
            content.stream().forEach(x -> {
                if (!StringUtils.isEmpty(x.getUpdateTime())) {
                    x.setUpdateTime(x.getUpdateTime().substring(0, 19));
                }
                if (!StringUtils.isEmpty(x.getCreateTime())) {
                    x.setCreateTime(x.getCreateTime().substring(0, 19));
                }
            });
        }
        return CommonResult.ok(bookingPage);
    }
}

