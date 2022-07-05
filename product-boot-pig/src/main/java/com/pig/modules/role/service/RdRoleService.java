package com.pig.modules.role.service;

import com.pig.modules.role.entity.RdRole;

import java.util.List;

/**
 * 角色表(RdRole)表服务接口
 *
 * @author makejava
 * @since 2022-07-05 23:28:07
 */
public interface RdRoleService {

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
     * @param ids ids
     * @return
     */
    List<RdRole> queryByIds(List<Integer> ids);

    /**
     * 新增数据
     *
     * @param rdRole 实例对象
     * @return 实例对象
     */
    RdRole insert(RdRole rdRole);

    /**
     * 修改数据
     *
     * @param rdRole 实例对象
     * @return 实例对象
     */
    RdRole update(RdRole rdRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
