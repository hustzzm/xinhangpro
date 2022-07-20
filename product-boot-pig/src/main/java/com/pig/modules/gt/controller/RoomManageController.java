package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.RoomManageDao;
import com.pig.modules.gt.entity.BizRoomManage;
import com.pig.modules.gt.service.BizRoomManageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 房间管理(RoomManage)表控制层
 *
 * @author makejava
 * @since 2022-06-28 21:53:13
 */
@RestController
@RequestMapping("/roomManage")
public class RoomManageController {

    @Resource
    private RoomManageDao roomManageDao;

    @Resource
    private BizRoomManageService bizRoomManageService;

    @GetMapping(value = "/list")
    public CommonResult test(@RequestParam Map<String, Object> params) {

        Page<BizRoomManage> usersPage = bizRoomManageService.page(params);
        return CommonResult.ok(usersPage);
    }


    @PostMapping("/save")
    public CommonResult save(@RequestBody BizRoomManage roomManage) {
        if (CommonUtil.checkObjAllFieldsIsNull(roomManage)) {
            return CommonResult.ok();
        }
        return bizRoomManageService.save(roomManage);
    }


    @PostMapping("/update")
    public CommonResult update(@RequestBody BizRoomManage roomManage) {
        if (CommonUtil.checkObjAllFieldsIsNull(roomManage)) {
            return CommonResult.ok();
        }
        return bizRoomManageService.update(roomManage);
    }

    /**
     * 删除
     */
    @DeleteMapping("/remove/{id}")
    public CommonResult remove(@PathVariable Integer id) {

        roomManageDao.deleteById(id);

        return CommonResult.ok("删除成功");
    }
}

