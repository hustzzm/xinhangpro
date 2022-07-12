package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.BizBooking;
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
 * (BizBooking)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
public interface BizBookingDao extends JpaRepository<BizBooking, Integer> {

    Page<BizBooking> findAll(Specification<BizBooking> specification, Pageable pageable);

    List<BizBooking> findAll(Specification<BizBooking> specification);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("delete from BizBooking where id in (:ids)")
    void deleteByIds(@Param("ids") List<Integer> ids);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizBooking set bookStatus = '3' where id =:id")
    void updateBookStatusById(@Param("id") Integer id);
}

