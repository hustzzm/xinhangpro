package com.pig.modules.room.dao;


import com.pig.modules.room.entity.BizRoomManage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * 房间管理的dao
 */
@Repository
public interface RoomManageDao extends JpaRepository<BizRoomManage, Integer> {

    Page<BizRoomManage> findAll(Specification<BizRoomManage> specification, Pageable pageable);

}
