package com.pig.modules.gt.api;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.RdDictDao;
import com.pig.modules.gt.dao.RoomManageDao;
import com.pig.modules.gt.entity.BizRoomManage;
import com.pig.modules.gt.entity.RdDict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wx/room")
public class RoomManageApi {

    @Resource
    private RoomManageDao roomManageDao;

    @Resource
    private RdDictDao dictDao;

    /**
     * 返回房间类型
     *
     * @return
     */
    @GetMapping("/allRoomType")
    public CommonResult roomType() {
        List<RdDict> rdDictList = dictDao.findByParentIdAndCodeAndIsDeleted("12", "roomtype", "0");
        List<String> collect = rdDictList.stream().map(RdDict::getDictValue).collect(Collectors.toList());
        return CommonResult.ok(collect);

    }

    @GetMapping("/allRoom")
    public CommonResult allRoom(@RequestParam Map<String, Object> params) {
        String roomType = StringUtil.getCheckString(params.get("roomType"));
        List<BizRoomManage> roomManageList = new ArrayList<>();
        if (StringUtil.isEmpty(roomType)) {
            roomManageList = roomManageDao.findAll();
        } else {
            roomManageList = roomManageDao.findByRoomTypeOrderByCreateTimeDesc(roomType);
        }
        return CommonResult.ok(roomManageList);

    }
}
