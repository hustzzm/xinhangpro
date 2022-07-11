package com.pig.modules.gt.service;

import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizRoomManage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

/**
 * (BizBooking)表服务接口
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
public interface BizBookingService {

    Page<BizBooking> page(Map<String, Object> params);
}
