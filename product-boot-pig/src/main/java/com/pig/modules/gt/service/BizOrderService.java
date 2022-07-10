package com.pig.modules.gt.service;

import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.entity.BizOrderExportVO;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * 订单信息表(BizOrder)表服务接口
 *
 * @author makejava
 * @since 2022-07-09 22:19:14
 */
public interface BizOrderService {
    Page<BizOrder> page(Map<String, Object> params);

    void exportData(ScrollResultsHandler<BizOrderExportVO> scrollResultsHandler);

    double getTotalAmount(Map<String, Object> params);
}
