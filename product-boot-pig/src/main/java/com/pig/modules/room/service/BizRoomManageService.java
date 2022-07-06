package com.pig.modules.room.service;

import org.springframework.data.domain.Page;
import com.pig.modules.room.entity.BizRoomManage;

import java.util.Map;

public interface BizRoomManageService {
    public Page<BizRoomManage> page(Map<String, Object> params);
}
