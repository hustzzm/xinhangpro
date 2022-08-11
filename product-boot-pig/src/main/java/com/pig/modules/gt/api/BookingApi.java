package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.core.BusinessUtil;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.service.BizBookingService;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/booking")
public class BookingApi {

    @Resource
    private BizBookingService bookingService;

    @Resource
    private BizBookingDao bookingDao;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
     * 查看房间已预约的记录
     *
     * @return CommonResult
     */
    @GetMapping("/existbook")
    public CommonResult existbook(@RequestParam Map<String, Object> params) {

        String roomCode = params.get("roomCode").toString();
        List<BizBooking> list = bookingService.getAllByBookAfterBookDate(roomCode);
        return CommonResult.ok(list);

    }

    /**
     * 根据订单号查询记录
     *
     * @return CommonResult
     */
    @GetMapping("/querybookbyno")
    public CommonResult querybookbyno(@RequestParam Map<String, Object> params) {

        String booksNo = params.get("booksNo").toString();
        BizBooking bizBooking = bookingService.findByWxOrderByBooksNo(booksNo);
        if(bizBooking != null){
            bizBooking.setBookTimesText(sdf.format(bizBooking.getBookDate()) + " " + BusinessUtil.getBookTimeText(bizBooking.getBookTimes()));
        }
        return CommonResult.ok(bizBooking);

    }

    @PostMapping("/cancel")
    public CommonResult cancel(@RequestBody Map<String, Object> params) {
        String openid = StringUtil.getCheckString(params.get("openid"));
        String booksNo = StringUtil.getCheckString(params.get("booksNo"));
        bookingDao.cancel(openid, booksNo);

        //取消预约后进行检索
        List<BizBooking> bizBookingList = bookingDao.findByWxOrderList(openid, "1");
        for(BizBooking bizBooking : bizBookingList){
            String bookContent = sdf.format(bizBooking.getBookDate()) + " " + BusinessUtil.getBookTimeText(bizBooking.getBookTimes());
            bizBooking.setBookTimesText(bookContent);
        }

        return CommonResult.ok(bizBookingList);

    }

    /**
     * 微信端会员删除已完成的预约记录
     * @param params
     * @return
     */
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody Map<String, Object> params) {

        String openid = StringUtil.getCheckString(params.get("openid"));
        String booksNo = StringUtil.getCheckString(params.get("booksNo"));
        bookingDao.disableOtherDelState(openid, booksNo);

        List<BizBooking> bizBookingList = bookingDao.findByWxOrderList(openid, "3");
        for(BizBooking bizBooking : bizBookingList){
            String bookContent = sdf.format(bizBooking.getBookDate()) + " " + BusinessUtil.getBookTimeText(bizBooking.getBookTimes());
            bizBooking.setBookTimesText(bookContent);
        }
        return CommonResult.ok(bizBookingList);
    }


    /**
     * 查看会员已预约的记录
     *
     * @return CommonResult
     */
    @GetMapping("/mybook")
    public CommonResult mybook(@RequestParam Map<String, Object> params) {

        String openid = StringUtil.getCheckString(params.get("openid"));
        String bookStatus = StringUtil.getCheckString(params.get("bookStatus"));
        List<BizBooking> bizBookingList = bookingDao.findByWxOrderList(openid, bookStatus);
        for(BizBooking bizBooking : bizBookingList){
            String bookContent = sdf.format(bizBooking.getBookDate()) + " " + BusinessUtil.getBookTimeText(bizBooking.getBookTimes());
            bizBooking.setBookTimesText(bookContent);
        }
        return CommonResult.ok(bizBookingList);

    }
}
