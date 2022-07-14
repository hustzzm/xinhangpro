package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.entity.BizRoomManage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单信息表(BizOrder)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-09 22:19:14
 */
public interface BizOrderDao extends JpaRepository<BizOrder, Integer> {
    Page<BizOrder> findAll(Specification<BizOrder> specification, Pageable pageable);

    List<BizOrder> findAll(Specification<BizOrder> specification);

    BizOrder findByOpenId(String openId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("delete from BizOrder where orderId in (:orderIds)")
    void deleteByOrderIds(@Param("orderIds") List<Integer> orderIds);
}

