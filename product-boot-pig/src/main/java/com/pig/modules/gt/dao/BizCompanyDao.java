package com.pig.modules.gt.dao;


import com.pig.modules.gt.entity.BizCompany;
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
 * 商家信息管理的dao
 */
@Repository
public interface BizCompanyDao extends JpaRepository<BizCompany, Integer> {

    Page<BizCompany> findAll(Specification<BizCompany> specification, Pageable pageable);

    BizCompany getBizCompanyById(Integer id);

}
