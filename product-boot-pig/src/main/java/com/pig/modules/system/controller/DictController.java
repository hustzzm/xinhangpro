package com.pig.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.TreeUtils;
import com.pig.modules.system.entity.Dict;
import com.pig.modules.system.entity.vo.DictVO;
import com.pig.modules.system.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    public CommonResult detail(Dict dict) {
        Dict detail = dictService.getById(dict.getId());
        return CommonResult.ok(detail);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public CommonResult list(@RequestParam Map<String, Object> dict) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>();
        if(!StringUtils.isEmpty(dict.get("code"))){
            queryWrapper.like("code", dict.get("code"));
        }
        if(!StringUtils.isEmpty(dict.get("dictValue"))){
            queryWrapper.like("dictValue", dict.get("dictValue"));
        }
        List<Dict> dictList = dictService.list(queryWrapper);
        List<DictVO> voList = new ArrayList<>();
        dictList.forEach(dictEntity -> {
            DictVO vo = new DictVO();
            BeanUtils.copyProperties(dictEntity,vo);
            voList.add(vo);
        });
        /**
         * 查询的时候不一定有root用户
         */
        List<Dict> collect = voList.stream().filter(dictVO -> dictVO.getParentId().equals("0")).collect(Collectors.toList());
        Collection<DictVO> roleVOS = null;
        if(collect.size()>0){
            roleVOS = TreeUtils.toTree(false,voList, "id", "parentId", "children", DictVO.class);
            return CommonResult.ok(roleVOS);
        }
        return CommonResult.ok(voList);
    }

    /**
     * 获取字典树形结构
     *
     * @return
     */
    @GetMapping("/tree")
    public CommonResult tree() {
        return dictService.dictTree();
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    public CommonResult submit(@Valid @RequestBody Dict dict) {
        dictService.submit(dict);
        String msg = "添加字典成功";
        if(!StringUtils.isEmpty(dict.getId())){
            msg = "修改字典成功";
        }
        return CommonResult.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public CommonResult remove(@RequestParam String ids) {
        dictService.removeByIds(CommonUtil.toLongList(ids));
        return CommonResult.ok();
    }

    /**
     * 获取字典
     *
     * @return
     */
    @GetMapping("/dictionary")
    public CommonResult dictionary(String code) {
        List<Dict> tree = dictService.getList(code);
        return CommonResult.ok(tree);
    }

    /**
     * 批量获取字典
     * @param codeStr
     * @return
     */
    @GetMapping("/batchDictionary")
    public CommonResult batchDictionary(String codeStr){
        Map<String, List<Dict>> batchDict = dictService.getBatchDict(Arrays.asList(codeStr.split(",")));
        return CommonResult.ok(batchDict);
    }

}
