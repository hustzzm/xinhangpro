package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.RdDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典表(RdDict)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-25 21:53:55
 */
@Repository
public interface RdDictDao extends JpaRepository<RdDict, Integer> {

    List<RdDict> findByParentIdAndCodeAndIsDeleted(@Param("parentId") String parentId, @Param("code") String code, @Param(
            "isDeleted") String isDeleted);
}

