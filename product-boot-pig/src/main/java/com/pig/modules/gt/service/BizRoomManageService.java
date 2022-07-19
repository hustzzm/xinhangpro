package com.pig.modules.gt.service;

import com.pig.basic.util.CommonResult;
import org.springframework.data.domain.Page;
import com.pig.modules.gt.entity.BizRoomManage;

import java.util.Map;

public interface BizRoomManageService {
    Page<BizRoomManage> page(Map<String, Object> params);

    CommonResult save(BizRoomManage roomManage);

    CommonResult update(BizRoomManage roomManage);
}
