package com.pig.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.modules.system.entity.DTO.TreeNode;
import com.pig.modules.system.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 获取字典树
     * @return
     */
    List<TreeNode> dictTree();

    /**
     * 获取字典表
     *
     * @param code 字典编号
     * @return
     */
    List<Dict> getList(String code);

    List<Dict> getBatchDict(List<String> codeList);

    String dictValue(@Param("param")Map<String,Object> param);
}
