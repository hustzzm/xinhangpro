package com.pig.modules.room.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.modules.room.entity.RoomManage;
import com.pig.modules.room.mapper.RoomManageMapper;
import com.pig.modules.room.service.RoomManageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 房间管理(RoomManage)表服务实现类
 *
 * @author makejava
 * @since 2022-06-28 21:53:16
 */
@Service("roomManageService")
public class RoomManageServiceImpl extends ServiceImpl<RoomManageMapper, RoomManage> implements RoomManageService {
    @Resource
    private RoomManageMapper roomManageMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RoomManage queryById(Integer id) {
        return this.roomManageMapper.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param roomManage 实例对象
     * @return 实例对象
     */
    @Override
    public RoomManage insert(RoomManage roomManage) {
        this.roomManageMapper.insert(roomManage);
        return roomManage;
    }

    /**
     * 修改数据
     *
     * @param roomManage 实例对象
     * @return 实例对象
     */
    @Override
    public RoomManage update(RoomManage roomManage) {
        this.roomManageMapper.update(roomManage);
        return this.queryById(roomManage.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.roomManageMapper.deleteById(id) > 0;
    }
}
