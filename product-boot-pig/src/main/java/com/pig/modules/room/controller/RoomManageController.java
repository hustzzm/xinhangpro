package com.pig.modules.room.controller;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.room.dao.RoomManageDao;
import com.pig.modules.room.entity.BizRoomManage;
import com.pig.modules.room.service.BizRoomManageService;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
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


    @PostMapping("/insertOrUpdate")
    public CommonResult update(@RequestBody BizRoomManage roomManage) {
        if (CommonUtil.checkObjAllFieldsIsNull(roomManage)) {
            return CommonResult.ok();
        }

        String msg = StringUtils.isEmpty(roomManage.getId()) ? "新增成功" : "修改成功";
        roomManageDao.save(roomManage);

        return CommonResult.ok(msg);
    }

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    public CommonResult remove(@RequestBody List<Integer> ids) {

        roomManageDao.deleteByIds(ids);

        return CommonResult.ok("删除成功");
    }
}

