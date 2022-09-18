package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.BizMember;
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
 * 用户信息
 * 不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
public interface BizMemberDao extends JpaRepository<BizMember, Integer> {

    BizMember findByOpenidAndStatus(String openid, String status);

    BizMember findByOpenidAndMobile(String openid, String mobile);

    Page<BizMember> findAll(Specification<BizMember> specification, Pageable pageable);
    List<BizMember> findAll(Specification<BizMember> specification);


    /**
     * 根据openId查询个人生效期内的会员记录
     *
     * @param openid
     * @param endDate 今天
     * @return
     */
    @Query(value = "select * from biz_member where openid=:openid and user_level !='0' and end_date>=:endDate and status='-1' limit 0,1", nativeQuery = true)
    BizMember findExistRecord(@Param("openid") String openid, @Param("endDate") String endDate);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizMember set orderNo = :orderNo where openid = :openId ")
    void updateOrderNoByOpenId(@Param("openId") String openId, @Param("orderNo") String orderNo);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizMember set status = :status where id = :id ")
    void updateByStatus(@Param("id") Integer id, @Param("status") String status);
}

