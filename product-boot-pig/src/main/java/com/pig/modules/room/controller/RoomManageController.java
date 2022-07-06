package com.pig.modules.room.controller;

import com.pig.basic.util.CommonResult;
import com.pig.modules.room.dao.RoomManageDao;
import com.pig.modules.room.entity.BizRoomManage;
import com.pig.modules.room.service.BizRoomManageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

//
//
//    /**
//     * 通过主键查询单条数据
//     *
//     * @param id 主键
//     * @return 单条数据
//     */
//    @GetMapping("{id}")
//    public ResponseEntity<RoomManage> queryById(@PathVariable("id") Integer id) {
//        return ResponseEntity.ok(this.roomManageService.queryById(id));
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param roomManage 实体
//     * @return 新增结果
//     */
//    @PostMapping
//    public ResponseEntity<RoomManage> add(RoomManage roomManage) {
//        return ResponseEntity.ok(this.roomManageService.insert(roomManage));
//    }
//
//    @PostMapping("/update")
//    public CommonResult update(@RequestBody RoomManage roomManage) {
//        if (StringUtils.isEmpty(roomManage.getId())) {
//            CommonResult commonResult = CommonResult.failed();
//            commonResult.setMsg("id不能为空，请检查！");
//            return commonResult;
//        }
//        UserVo userVo = CommonUtil.getUserVoFormToken();
//        roomManage.setCreateBy(userVo.getUserCode());
//
//        roomManageService.update(roomManage);
//
//        return CommonResult.ok("修改成功");
//
//    }
//
//
//    /**
//     * 删除数据
//     *
//     * @param id 主键
//     * @return 删除是否成功
//     */
//    @DeleteMapping
//    public ResponseEntity<Boolean> deleteById(Integer id) {
//        return ResponseEntity.ok(this.roomManageService.deleteById(id));
//    }

}

