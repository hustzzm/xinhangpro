package com.pig.modules.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.TreeUtils;
import com.pig.modules.system.entity.DTO.TreeNode;
import com.pig.modules.system.entity.Dict;
import com.pig.modules.system.mapper.DictMapper;
import com.pig.modules.system.service.DictService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    
    

    @Override
    public CommonResult dictTree() {
        List<TreeNode> treeNodes = baseMapper.dictTree();
        Collection<TreeNode> roleVos = TreeUtils.toTree(false,treeNodes, "id", "parentId", "children", TreeNode.class);
        return CommonResult.ok(roleVos);
    }

    @Override
    public boolean submit(Dict dict) {
        LambdaQueryWrapper<Dict> lqw = Wrappers.<Dict>query().lambda().eq(Dict::getCode, dict.getCode()).eq(Dict::getDictKey, dict.getDictKey());
        Integer cnt = baseMapper.selectCount((StringUtils.isEmpty(dict.getId())) ? lqw : lqw.notIn(Dict::getId, dict.getId()));
        if (cnt > 0) {
            throw new ApiException("当前字典键值已存在!");
        }
        return saveOrUpdate(dict);
    }

    @Override
    public List<Dict> getList(String code) {
        return baseMapper.getList(code);
    }

    @Override
    public Map<String, List<Dict>> getBatchDict(List<String> codeList) {
        List<Dict> batchDict = baseMapper.getBatchDict(codeList);
        Map<String, List<Dict>> collect = batchDict.stream()
                .collect(Collectors.groupingBy(Dict::getCode));
        collect.values()
                .forEach(list -> Collections.sort(list, Comparator.comparingInt(Dict::getSort)));
        return collect;
    }
}
