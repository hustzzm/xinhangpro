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

    List<BizBooking> findByOpenidAndBookStatusOrderByCreateTimeDesc(String openid, String bookStatus);

    /** 针对含有9的记录 **/
    @Query(value ="select count(1) from biz_booking where book_date =?1 and (book_times =?2 or book_times like  ?1||'%') and book_status='1'", nativeQuery = true)
    int querylistByTime(String bookDate, String bookTimes,String bookTimes2);

    /** 针对不含有9的记录 **/
    @Query(value ="select count(1) from biz_booking where book_date =?1 and (book_times =?2 or book_times like  '%'||?1||',%') and book_status='1'", nativeQuery = true)
    int querylistByNormalTime(String bookDate, String bookTimes);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("delete from BizBooking where id in (:ids)")
    void deleteByIds(@Param("ids") List<Integer> ids);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizBooking set bookStatus = '3' where id =:id")
    void updateBookStatusById(@Param("id") Integer id);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizBooking set bookStatus = '5' where openid =:openid and booksNo = :booksNo")
    void cancel(@Param("openid") String openid, @Param("booksNo") String booksNo);
}

