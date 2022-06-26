package com.pig.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.modules.system.entity.DTO.TreeNode;
import com.pig.modules.system.entity.Dept;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取部门树
     * @return
     */
    List<TreeNode> deptTree();

}
