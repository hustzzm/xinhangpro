package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.BizCompany;
import com.pig.modules.gt.entity.BizMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息
 * 不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
public interface BizMemberDao extends JpaRepository<BizMember, Integer> {

    BizMember findByOpenidAndStatus(String openid, String status);

    Page<BizMember> findAll(Specification<BizMember> specification, Pageable pageable);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizMember set orderNo = :orderNo where openid = :openId ")
    void updateOrderNoByOpenId(@Param("openId") String openId, @Param("orderNo") String orderNo);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizMember set status = :status where id = :id ")
    void updateByStatus(@Param("id") Integer id, @Param("status") String status);
}

