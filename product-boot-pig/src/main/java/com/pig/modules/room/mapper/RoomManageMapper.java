package com.pig.modules.room.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.modules.room.entity.RoomManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房间管理(RoomManage)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-28 21:53:14
 */
public interface RoomManageMapper extends BaseMapper<RoomManage> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RoomManage queryById(Integer id);

    /**
     * 统计总行数
     *
     * @param roomManage 查询条件
     * @return 总行数
     */
    long count(RoomManage roomManage);

    /**
     * 新增数据
     *
     * @param roomManage 实例对象
     * @return 影响行数
     */
    int insert(RoomManage roomManage);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RoomManage> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RoomManage> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RoomManage> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<RoomManage> entities);

    /**
     * 修改数据
     *
     * @param roomManage 实例对象
     * @return 影响行数
     */
    int update(RoomManage roomManage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

