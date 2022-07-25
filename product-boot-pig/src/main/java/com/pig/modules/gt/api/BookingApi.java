package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.service.BizBookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/wx/booking")
public class BookingApi {

    @Resource
    private BizBookingService bookingService;

    /**
     * 预约
     *
     * @param params params
     * @return CommonResult
     */
    @PostMapping("/submit")
    public CommonResult make(@RequestBody Map<String, Object> params) {

        return bookingService.make(params);

    }
}
