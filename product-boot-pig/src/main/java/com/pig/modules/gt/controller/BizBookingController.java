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
import java.util.HashMap;
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

        Map<String,Object> timesMap = new HashMap<>();
        timesMap.put("9","9:00-10:00");
        timesMap.put("10","10:00-11:00");
        timesMap.put("11","11:00-12:00");
        timesMap.put("12","12:00-13:00");
        timesMap.put("13","13:00-14:00");
        timesMap.put("14","14:00-15:00");
        timesMap.put("15","15:00-16:00");
        timesMap.put("16","16:00-17:00");
        timesMap.put("17","17:00-18:00");
        timesMap.put("18","18:00-19:00");
        timesMap.put("19","19:00-20:00");
        timesMap.put("20","20:00-21:00");

       if(bookingPage.getContent() != null && bookingPage.getContent().size() > 0){

           for(int i = 0; i < bookingPage.getContent().size();i++){

               String bookTimes = bookingPage.getContent().get(i).getBookTimes();
               if(timesMap.get(bookTimes) != null) {
                   bookingPage.getContent().get(i).setBookTimesText(timesMap.get(bookTimes).toString());
               }
           }
       }



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

