package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.BizOrder;
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

    BizOrder findByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据openId查询本人的所有订单
     *
     * @param openId
     * @return
     */
    @Query(value = "select * from biz_order where open_id=:openId order by create_time desc", nativeQuery = true)
    List<BizOrder> findByOpenId(@Param("openId") String openId);

    /**
     * 查询一条未语音播报的新订单
     * 订单状态=10
     * @return
     */
    @Query(value = "select * from biz_order where order_status='10' and sound_state='-1' order by create_time desc limit 0,1", nativeQuery = true)
    BizOrder findByUnSoundState();

    /**
     * 更新语音播报的记录为已播报
     * 订单状态=10
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update biz_order set sound_state='1' where order_id=:orderId", nativeQuery = true)
    void updateByUnSoundState(@Param("orderId") Integer orderId);

    /**
     * 查找是否有未支付的订单
     *
     * @param openId
     * @return
     */
    @Query(value = "select * from biz_order where open_id=:openId and order_status!='10' and status='-1' limit 0,1", nativeQuery = true)
    BizOrder findByOpenIdAndNoPay(@Param("openId") String openId);

    /**
     * 查找有效的订单，未过期
     *
     * @param openId
     * @return
     */
    @Query(value = "select * from biz_order where open_id=:openId and order_status='10' and order_end>:orderEnd and status='-1' limit 0,1", nativeQuery = true)
    BizOrder findByOpenIdAndNoExpired(@Param("openId") String openId, @Param("orderEnd") String orderEnd);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("delete from BizOrder where orderId in (:orderIds)")
    void deleteByOrderIds(@Param("orderIds") List<Integer> orderIds);

    /**
     * 更新记录为作废
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizOrder set status='0' where orderId=:orderId")
    void updateByOrderStatus(@Param("orderId") Integer orderId);
}

