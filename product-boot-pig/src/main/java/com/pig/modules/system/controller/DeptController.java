package com.pig.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.TreeUtils;
import com.pig.modules.system.entity.Dept;
import com.pig.modules.system.entity.vo.DeptVO;
import com.pig.modules.system.service.DeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    public CommonResult detail(Dept dept) {
        Dept detail = deptService.getById(dept.getId());
        return CommonResult.ok(detail);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public CommonResult list(@RequestParam Map<String, Object> dept) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<Dept>();
        if(!StringUtils.isEmpty(dept.get("deptName"))){
            queryWrapper.like("dept_name", dept.get("deptName"));
        }
        if(!StringUtils.isEmpty(dept.get("fullName"))){
            queryWrapper.like("full_name", dept.get("fullName"));
        }
        List<Dept> deptList = deptService.list(queryWrapper);
        List<DeptVO> voList = new ArrayList<>();
        deptList.forEach(deptEntity -> {
            DeptVO vo = new DeptVO();
            BeanUtils.copyProperties(deptEntity,vo);
            voList.add(vo);
        });
        /**
         * 查询的时候不一定有root用户
         */
        List<Dept> collect = voList.stream().filter(deptVO -> deptVO.getParentId().equals("0")).collect(Collectors.toList());
        Collection<DeptVO> deptVOS = null;
        if(collect.size()>0){
            deptVOS = TreeUtils.toTree(false,voList, "id", "parentId", "children", DeptVO.class);
            return CommonResult.ok(deptVOS);
        }
        return CommonResult.ok(deptList);
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public CommonResult remove(@RequestParam String ids) {
        deptService.removeByIds(CommonUtil.toLongList(ids));
        return CommonResult.ok("删除成功");
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    public CommonResult submit(@RequestBody Dept dept) {
        deptService.saveOrUpdate(dept);
        String msg = "添加部门成功";
        if(!StringUtils.isEmpty(dept.getId())){
            msg = "修改部门成功";
        }
        return CommonResult.ok(msg);
    }

    /**
     * 获取部门树形结构
     *
     * @return
     */
    @GetMapping("/tree")
    public CommonResult tree() {
        return deptService.deptTree();
    }
}
