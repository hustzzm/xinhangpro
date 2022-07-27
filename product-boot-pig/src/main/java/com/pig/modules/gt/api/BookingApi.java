package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.service.BizBookingService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/booking")
public class BookingApi {

    @Resource
    private BizBookingService bookingService;

    @Resource
    private BizBookingDao bookingDao;

    /**
     * 预约
     *
     * @param params params
     * @return CommonResult
     */
    @PostMapping("/submit")
    public CommonResult make(@RequestBody Map<String, Object> params) {

        return bookingService.booksave(params);
//        return bookingService.make(params);

    }

    /**
     * 查看已预约的记录
     *
     * @param params params
     * @return CommonResult
     */
    @GetMapping("/existbook")
    public CommonResult existbook(@RequestParam Map<String, Object> params) {
        params.put("apiType", "-1");
        Page<BizBooking> page = bookingService.page(params);
        return CommonResult.ok(page);

    }

    @PostMapping("/cancel")
    public CommonResult cancel(@RequestParam Map<String, Object> params) {
        String openid = StringUtil.getCheckString(params.get("openid"));
        String booksNo = StringUtil.getCheckString(params.get("booksNo"));
        bookingDao.cancel(openid, booksNo);
        return CommonResult.ok();

    }

    @GetMapping("/mybook")
    public CommonResult mybook(@RequestParam Map<String, Object> params) {
        String openid = StringUtil.getCheckString(params.get("openid"));
        String bookStatus = StringUtil.getCheckString(params.get("bookStatus"));
        List<BizBooking> bizBookingList = bookingDao.findByOpenidAndBookStatusOrderByCreateTimeDesc(openid, bookStatus);
        return CommonResult.ok(bizBookingList);

    }
}
