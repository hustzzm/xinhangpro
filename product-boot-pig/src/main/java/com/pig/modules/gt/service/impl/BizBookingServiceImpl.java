package com.pig.modules.gt.service.impl;

import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.service.BizBookingService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (BizBooking)表服务实现类
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
@Service("bizBookingService")
public class BizBookingServiceImpl implements BizBookingService {
    @Resource
    private BizBookingDao bizBookingDao;
}
