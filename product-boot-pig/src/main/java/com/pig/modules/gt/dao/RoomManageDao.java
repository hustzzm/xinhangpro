package com.pig.modules.gt.dao;


import com.pig.modules.gt.entity.BizRoomManage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 房间管理的dao
 */
@Repository
public interface RoomManageDao extends JpaRepository<BizRoomManage, Integer> {

    Page<BizRoomManage> findAll(Specification<BizRoomManage> specification, Pageable pageable);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("delete from BizRoomManage where id in (:ids)")
    void deleteByIds(@Param("ids") List<Integer> ids);

    @Query("select MAX(roomCode) from BizRoomManage")
    String getMaxRoomCode();

    BizRoomManage findByRoomCode(String roomCode);
}
