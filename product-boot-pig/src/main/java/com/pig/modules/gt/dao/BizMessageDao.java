package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizMessage;
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
 * (BizMessage)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
public interface BizMessageDao extends JpaRepository<BizMessage, Integer> {

    @Query(value ="select * from biz_message where openid=:openid and status='-1' order by id desc", nativeQuery = true)
    List<BizMessage> findAllMessage(@Param("openid") String openid);


    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizMessage set finishstate = :finishstate where id =:id")
    void updatebyid(@Param("id") String id, @Param("finishstate") String finishstate);

}

