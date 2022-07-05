package com.pig.modules.role.service.impl;

import com.pig.modules.role.mapper.RdRoleMapper;
import com.pig.modules.role.entity.RdRole;
import com.pig.modules.role.service.RdRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色表(RdRole)表服务实现类
 *
 * @author makejava
 * @since 2022-07-05 23:28:08
 */
@Service("rdRoleService")
public class RdRoleServiceImpl implements RdRoleService {
    @Resource
    private RdRoleMapper rdRoleMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RdRole queryById(Long id) {
        return this.rdRoleMapper.queryById(id);
    }

    @Override
    public List<RdRole> queryByIds(List<Integer> ids) {
        return this.rdRoleMapper.queryByIds(ids);
    }


    /**
     * 新增数据
     *
     * @param rdRole 实例对象
     * @return 实例对象
     */
    @Override
    public RdRole insert(RdRole rdRole) {
        this.rdRoleMapper.insert(rdRole);
        return rdRole;
    }

    /**
     * 修改数据
     *
     * @param rdRole 实例对象
     * @return 实例对象
     */
    @Override
    public RdRole update(RdRole rdRole) {
        this.rdRoleMapper.update(rdRole);
        return this.queryById(rdRole.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.rdRoleMapper.deleteById(id) > 0;
    }
}
