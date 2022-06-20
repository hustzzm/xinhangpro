package com.pig.modules.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.TreeUtils;
import com.pig.modules.system.entity.DTO.TreeNode;
import com.pig.modules.system.entity.Dept;
import com.pig.modules.system.mapper.DeptMapper;
import com.pig.modules.system.service.DeptService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public CommonResult deptTree() {
        List<TreeNode> treeNodes = baseMapper.deptTree();
        Collection<TreeNode> roleVos = TreeUtils.toTree(false,treeNodes, "id", "parentId", "children", TreeNode.class);
        return CommonResult.ok(roleVos);
    }
}
