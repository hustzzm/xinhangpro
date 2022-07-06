package com.pig.modules.room.service.impl;

import com.pig.basic.util.CommonQuery;
import com.pig.modules.room.dao.RoomManageDao;
import com.pig.modules.room.entity.BizRoomManage;
import com.pig.modules.room.service.BizRoomManageService;
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
        Pageable pageable = PageRequest.of(commonQuery.getCurrent() - 1, commonQuery.getSize(), Sort.by(Sort.Direction.DESC,
                "createTime"));
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
}
