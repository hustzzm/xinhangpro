package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.service.BizBookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        Page<BizBooking> bookingPage = bizBookingService.page(params);

       if(bookingPage.getContent() != null && bookingPage.getContent().size() > 0){

           for(int i = 0; i < bookingPage.getContent().size();i++){

               String bookTimes = bookingPage.getContent().get(i).getBookTimes();
               String bookDate = sdf.format(bookingPage.getContent().get(i).getBookDate());
               bookingPage.getContent().get(i).setBookTimesText(bookDate + " " + getBookTimeText(bookTimes));
           }
       }



        return CommonResult.ok(bookingPage);
    }

    private String getBookTimeText(String bookTimes){

        String result = "";
        if(StringUtil.isNull(bookTimes)){
            return result;
        }

        String[] arr = bookTimes.split(",");
        if(arr.length == 1){
            int itmpval = StringUtil.getCheckInteger(bookTimes);

            result = String.valueOf(itmpval) + ":00-" + String.valueOf(itmpval+1) + ":00";
        }else{
            int itmpval = StringUtil.getCheckInteger(arr[0]);
            int itmpval2 = StringUtil.getCheckInteger(arr[arr.length -1]);

            result = String.valueOf(itmpval) + ":00-" + String.valueOf(itmpval2 + 1) + ":00";
        }

        return result;
    }

    @PostMapping(value = "/finished/{id}")
    public CommonResult finished(@PathVariable Integer id) {
        bizBookingDao.updateBookStatusById(id);
        return CommonResult.ok("预约已完成！");
    }

    @PostMapping(value = "/remove/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        bizBookingDao.disableStatusById(id);
        return CommonResult.ok("预约已删除！");
    }
}

