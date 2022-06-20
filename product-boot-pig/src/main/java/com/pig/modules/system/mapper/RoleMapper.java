package com.pig.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.modules.system.entity.DTO.TreeNode;
import com.pig.modules.system.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取角色树
     * @return
     */
    List<TreeNode> roleTree();

}
