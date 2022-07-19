package com.pig.modules.gt.service.impl;

import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.utils.JpaUtil;
import com.pig.modules.gt.dao.RoomManageDao;
import com.pig.modules.gt.entity.BizRoomManage;
import com.pig.modules.gt.service.BizRoomManageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.Map;


@Service
public class BizRoomManageServiceImpl implements BizRoomManageService {

    @Resource
    private RoomManageDao roomManageDao;

    @Override
    public Page<BizRoomManage> page(Map<String, Object> params) {
        //分页，以及排序方式
        //注意点：pageNo 是从0开始的，pageSize是当前页查询个数
        CommonQuery commonQuery = new CommonQuery(params);
        Pageable pageable = PageRequest.of(commonQuery.getCurrent() - 1, commonQuery.getSize(),
                Sort.by(Sort.Direction.ASC, "createTime"));
        Specification<BizRoomManage> specification = (root, criteriaQuery, criteriaBuilder) -> {
            //增加筛选条件
            Predicate predicate = criteriaBuilder.conjunction();
            //name不为空
            if (!StringUtils.isEmpty(commonQuery.get("name"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + commonQuery.get("name") + "%"));
            }
            if (!StringUtils.isEmpty(commonQuery.get("roomType"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("roomType"), "%" + commonQuery.get("roomType") + "%"));
            }
            return predicate;
        };
        return roomManageDao.findAll(specification, pageable);
    }

    @Override
    public CommonResult save(BizRoomManage roomManage) {
        roomManage.setRoomCode(getRoomCode());

        roomManageDao.save(roomManage);

        return CommonResult.ok("新增成功");
    }

    @Override
    public CommonResult update(BizRoomManage roomManage) {
        if (roomManage != null && roomManage.getId() != null) {
            BizRoomManage roomManageBak = roomManageDao.getOne(roomManage.getId());
            if (roomManageBak != null) {
                JpaUtil.copyNotNullProperties(roomManage, roomManageBak);
            }
            roomManageDao.save(roomManageBak);
        }
        return CommonResult.ok("修改成功！");
    }

    /**
     * 生成房间编号，默认1001
     *
     * @return int
     */
    private String getRoomCode() {
        String maxRoomCode = roomManageDao.getMaxRoomCode();
        if (StringUtils.isEmpty(maxRoomCode)) {
            return "1001";
        }
        return Integer.valueOf(maxRoomCode) + 1 + "";
    }
}
