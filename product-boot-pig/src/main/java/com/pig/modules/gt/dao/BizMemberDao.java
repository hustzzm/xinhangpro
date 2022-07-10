package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.BizOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户信息
 * 不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
public interface BizMemberDao extends JpaRepository<BizOrder, Integer> {
}

