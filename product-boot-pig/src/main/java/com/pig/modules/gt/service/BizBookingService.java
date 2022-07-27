package com.pig.modules.gt.service;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.entity.BizBooking;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * (BizBooking)表服务接口
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
public interface BizBookingService {

    Page<BizBooking> page(Map<String, Object> params);

    CommonResult make(Map<String, Object> params);

    /**
     * 查询所有已预约的记录
     * @return
     */
    List<BizBooking> getAllByBookAfterBookDate(String roomId);
    /**
     * 判断是否可预约，可则保存预约记录
     * @param params
     * @return
     */
    CommonResult booksave(Map<String, Object> params);

}
