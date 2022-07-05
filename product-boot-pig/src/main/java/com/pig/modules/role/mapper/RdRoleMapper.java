package com.pig.modules.role.mapper;

import com.pig.modules.role.entity.RdRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表(RdRole)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-05 23:28:05
 */
public interface RdRoleMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RdRole queryById(Long id);

    /**
     * 根据主键获取
     *
     * @param ids
     * @return
     */
    List<RdRole> queryByIds(@Param("ids") List<Integer> ids);


    /**
     * 统计总行数
     *
     * @param rdRole 查询条件
     * @return 总行数
     */
    long count(RdRole rdRole);

    /**
     * 新增数据
     *
     * @param rdRole 实例对象
     * @return 影响行数
     */
    int insert(RdRole rdRole);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RdRole> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RdRole> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RdRole> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<RdRole> entities);

    /**
     * 修改数据
     *
     * @param rdRole 实例对象
     * @return 影响行数
     */
    int update(RdRole rdRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

