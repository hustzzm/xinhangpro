package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.core.BusinessUtil;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.service.BizBookingService;
import org.springframework.data.domain.Page;

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
               bookingPage.getContent().get(i).setBookTimesText(bookDate + " " + BusinessUtil.getBookTimeText(bookTimes));
           }
       }

        return CommonResult.ok(bookingPage);
    }

    /**
     * 查询新的一条未语音播报的新订单
     * @return
     */
    @GetMapping(value = "/queryNewBooking")
    public CommonResult queryNewBooking() {

        BizBooking bizBooking = bizBookingDao.findBookByUnSoundState();
        if(bizBooking == null || StringUtil.isNull(bizBooking.getBooksNo())){
            CommonResult.failed();
        }
        return CommonResult.ok(bizBooking);
    }

    /**
     * 声音播报完成后，更新该记录的声音状态未已播报
     */
    @GetMapping("/updateSoundState")
    public CommonResult updateSoundState(@RequestParam String id) {

//        orderDao.deleteById(id);
        bizBookingDao.updateByUnSoundState(Integer.parseInt(id));
        return CommonResult.ok("删除成功");
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

