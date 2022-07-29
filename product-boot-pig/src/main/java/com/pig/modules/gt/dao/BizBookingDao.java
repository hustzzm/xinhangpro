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

    /**
     * 查询当前已预约的记录
     * @return
     */
    @Query(value ="select * from biz_booking where room_code=:roomCode and book_date>=:bookDate and (book_status ='1' or book_status='3') and status='-1'", nativeQuery = true)
    List<BizBooking> querylistallbybookStatus(@Param("bookDate") String bookDate,@Param("roomCode") String roomId);

    /**
     * 查询当天预约、消费的记录
     * @param bookDate
     * @return
     */
    @Query(value ="select * from biz_booking where openid=:openid and book_date =:bookDate and book_status !='5' and status='-1'", nativeQuery = true)
    List<BizBooking> querylistbybookDate(@Param("bookDate") String bookDate,@Param("openid") String openid);


    /** 针对含有9的记录 **/
    @Query(value ="select count(1) from biz_booking where book_date =:bookDate and (book_times =:bookTimes or book_times like CONCAT(:bookTimes2,'%'))) and (book_status='1' or book_status='3') and status='-1'", nativeQuery = true)
    int querylistByTime(@Param("bookDate") String bookDate, @Param("bookTimes") String bookTimes,@Param("bookTimes2") String bookTimes2);

    /** 针对不含有9的记录 **/
    @Query(value ="select count(1) from biz_booking where book_date =:bookDate and (book_times like CONCAT('%',:bookTimes,'%')) and (book_status='1' or book_status='3') and status='-1'", nativeQuery = true)
    int querylistByNormalTime(@Param("bookDate") String bookDate,@Param("bookTimes") String bookTimes);

    /**
     * 当前日期已过期的记录，用于自动将状态置为消费完成
     * @param bookDate
     * @return
     */
    @Query(value ="select * from biz_booking where book_date <:bookDate and book_status ='1' and status='-1'", nativeQuery = true)
    List<BizBooking> querylistbyexpireDate(@Param("bookDate") String bookDate);

    /**
     * 当天的小时已过期的记录，用于自动将状态置为消费完成
     * @param bookDate
     * @return
     */
    @Query(value ="select a.* from (select id,books_no,name,openid,nick_name,room_type,room_name,book_date,case when LOCATE(',',book_times) > 0 then REVERSE(LEFT(REVERSE(book_times),INSTR(REVERSE(book_times),',')-1)) else book_times end as book_times,room_code,book_status,create_by,create_time,update_by,update_time,status,book_times_text,account from biz_booking where book_date=:bookDate and (book_status ='1') and status='-1') a where a.book_times<:bookTimes", nativeQuery = true)
    List<BizBooking> querylistbyexpireHour(@Param("bookDate") String bookDate,@Param("bookTimes") String bookTimes);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("delete from BizBooking where id in (:ids)")
    void deleteByIds(@Param("ids") List<Integer> ids);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizBooking set status = '0' where id =:id")
    void disableStatusById(@Param("id") Integer id);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizBooking set bookStatus = '5' where openid =:openid and booksNo = :booksNo")
    void cancel(@Param("openid") String openid, @Param("booksNo") String booksNo);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update BizBooking set bookStatus = '3' where id =:id")
    void updateBookStatusById(@Param("id") Integer id);
}

