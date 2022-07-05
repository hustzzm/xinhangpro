package com.pig.modules.room.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.StringUtil;
import com.pig.modules.role.entity.RdRole;
import com.pig.modules.role.service.RdRoleService;
import com.pig.modules.room.entity.RoomManage;
import com.pig.modules.room.service.RoomManageService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 房间管理(RoomManage)表控制层
 *
 * @author makejava
 * @since 2022-06-28 21:53:13
 */
@RestController
@RequestMapping("/roomManage")
public class RoomManageController {
    /**
     * 服务对象
     */
    @Resource
    private RoomManageService roomManageService;

    @Resource
    private RdRoleService roleService;

    /**
     * 用户列表
     */
    @GetMapping(value = "/list", produces = "application/json;charset=utf-8")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        CommonQuery commonQuery = new CommonQuery(params);
        QueryWrapper<RoomManage> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(commonQuery.get("name"))) {
            queryWrapper.like("name", commonQuery.get("name"));
        }
        if (!StringUtils.isEmpty(commonQuery.get("roomType"))) {
            queryWrapper.like("room_type", commonQuery.get("roomType"));
        }
        if (!StringUtils.isEmpty(commonQuery.get("current"))) {
            commonQuery.setCurrent((int) (params.get("current")));
        }
        if (!StringUtils.isEmpty(commonQuery.get("size"))) {
            commonQuery.setCurrent((int) (params.get("size")));
        }
        queryWrapper.orderByAsc("create_time");
        Page<RoomManage> pageBP = new Page<>(commonQuery.getCurrent(), commonQuery.getSize());  // 查询第1页，每页返回10条
        IPage<RoomManage> pages = roomManageService.page(pageBP, queryWrapper);
        List<Integer> roleIds = pages.getRecords().stream().map(RoomManage::getRoleType).collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(roleIds)) {
            List<RdRole> rdRoleList = roleService.queryByIds(roleIds);
            Map<Integer, String> map = new HashMap<>();
            for (RdRole rdRole : rdRoleList) {
                map.put(Math.toIntExact(rdRole.getId()), rdRole.getRoleName());
            }
            // 设置角色名称
            pages.getRecords().stream().forEach(x -> {
                x.setRoleName(map.get(x.getRoleType()));
            });
        }
        return CommonResult.ok(pages);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<RoomManage> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.roomManageService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param roomManage 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<RoomManage> add(RoomManage roomManage) {
        return ResponseEntity.ok(this.roomManageService.insert(roomManage));
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody Map<String, Object> params) {

        UserVo userVo = CommonUtil.getUserVoFormToken();
        params.put("updateUser", userVo.getUserCode());
        params.put("updateTime", new Date());

        // 获取主键
        int id = ((Double) params.get("id")).intValue();
        RoomManage roomManage = roomManageService.getById(id);

        roomManage.setUpdateBy(userVo.getUserCode());
        roomManage.setName(StringUtil.getCheckString(params.get("name")));


        roomManageService.update(roomManage);

        return CommonResult.ok("修改成功");

    }


    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.roomManageService.deleteById(id));
    }

}

