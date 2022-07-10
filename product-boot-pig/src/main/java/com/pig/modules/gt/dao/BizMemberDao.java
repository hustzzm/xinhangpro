package com.pig.modules.gt.dao;

import com.pig.modules.gt.entity.BizMember;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 用户信息
不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
public interface BizMemberDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BizMember queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param bizMember 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<BizMember> queryAllByLimit(BizMember bizMember, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param bizMember 查询条件
     * @return 总行数
     */
    long count(BizMember bizMember);

    /**
     * 新增数据
     *
     * @param bizMember 实例对象
     * @return 影响行数
     */
    int insert(BizMember bizMember);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<BizMember> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BizMember> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<BizMember> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<BizMember> entities);

    /**
     * 修改数据
     *
     * @param bizMember 实例对象
     * @return 影响行数
     */
    int update(BizMember bizMember);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}

