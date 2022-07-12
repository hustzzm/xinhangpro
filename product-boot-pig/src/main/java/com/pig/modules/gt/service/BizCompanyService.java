package com.pig.modules.gt.service;

import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

/**
 * 房间管理(BizCompany)表服务接口
 *
 * @author makejava
 * @since 2022-07-12 23:32:41
 */
public interface BizCompanyService {
    Page<BizCompany> page(Map<String, Object> params);
}
