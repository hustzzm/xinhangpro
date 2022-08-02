package com.pig.modules.task;

import cn.hutool.core.date.DateTime;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.service.BizBookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author :  linqi
 * @date :  2022/5/24
 */
@Slf4j
@Component
public class CommonDataTask {

    @Resource
    private BizBookingService bizBookingService;
    @Resource
    private BizBookingDao bizBookingDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private SimpleDateFormat df = new SimpleDateFormat("HH");
    /**
     * 每10分钟执行一次，预定状态更新
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void backup(){
        //获取过期的样本数据
        log.info("CommonDataTask Scheduled start ----------------");

        Date dateNow = new Date();
        String today = sdf.format(dateNow);
        String Hour = Integer.valueOf(df.format(dateNow)).toString();

        List<BizBooking> list = bizBookingService.querylistbyexpireDate(today);
        for(BizBooking bizBooking : list){
            bizBookingDao.updateBookStatusById(bizBooking.getId());
        }

        List<BizBooking> hourList = bizBookingService.querylistbyexpireHour(today,Hour);
        if(hourList != null && hourList.size() > 0) {
            for (BizBooking bizBooking : hourList) {
                bizBookingDao.updateBookStatusById(bizBooking.getId());
            }
        }
        log.info("CommonDataTask do backup :" + new Date());

        log.info("CommonDataTask Scheduled end ----------------");
    }
}
