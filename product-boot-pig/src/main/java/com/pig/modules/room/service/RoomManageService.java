package com.pig.modules.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.modules.room.entity.RoomManage;

/**
 * 房间管理(RoomManage)表服务接口
 *
 * @author makejava
 * @since 2022-06-28 21:53:15
 */
public interface RoomManageService extends IService<RoomManage> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RoomManage queryById(Integer id);

    /**
     * 新增数据
     *
     * @param roomManage 实例对象
     * @return 实例对象
     */
    RoomManage insert(RoomManage roomManage);

    /**
     * 修改数据
     *
     * @param roomManage 实例对象
     * @return 实例对象
     */
    RoomManage update(RoomManage roomManage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
