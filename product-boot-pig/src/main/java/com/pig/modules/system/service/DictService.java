package com.pig.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.basic.util.CommonResult;
import com.pig.modules.system.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface DictService extends IService<Dict> {

    /**
     * 获取字典树
     * @return
     */
    CommonResult dictTree();


    /**
     * 新增或修改
     * @param dict
     * @return
     */
    boolean submit(Dict dict);

    /**
     * 获取字典表
     *
     * @param code 字典编号
     * @return
     */
    List<Dict> getList(String code);

    /**
     * 批量获取字典
     * @param codeList
     * @return
     */
    Map<String,List<Dict>> getBatchDict(List<String> codeList);

}
