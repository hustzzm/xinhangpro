package com.pig.modules.gt.service.impl;

import com.pig.basic.util.CommonQuery;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.dao.BizBookingDao;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.entity.BizRoomManage;
import com.pig.modules.gt.service.BizBookingService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (BizBooking)表服务实现类
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
@Service("bizBookingService")
public class BizBookingServiceImpl implements BizBookingService {
    @Resource
    private BizBookingDao bizBookingDao;

    @Override
    public Page<BizBooking> page(Map<String, Object> params) {
        //分页，以及排序方式
        //注意点：pageNo 是从0开始的，pageSize是当前页查询个数
        CommonQuery commonQuery = new CommonQuery(params);
        Pageable pageable = PageRequest.of(commonQuery.getCurrent() - 1, commonQuery.getSize(),
                Sort.by(Sort.Direction.ASC, "createTime"));
        Specification<BizBooking> specification = (root, criteriaQuery, criteriaBuilder) -> {
            // 封装查询条件
            List<Predicate> list = new ArrayList<>();

            Join join = root.join("bizMember", JoinType.LEFT);
            if (!StringUtils.isEmpty(commonQuery.get("name"))) {
                Predicate predicateJoin = criteriaBuilder.like(join.get("name"), "%" + commonQuery.get("name") + "%");
                list.add(predicateJoin);
            }
            //增加筛选条件
            // 房间名
            Predicate predicate = criteriaBuilder.conjunction();
            if (!StringUtils.isEmpty(commonQuery.get("roomName"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("roomName"), "%" + commonQuery.get("roomName") + "%"));
            }
            // 预约状态
            if (!StringUtils.isEmpty(commonQuery.get("bookStatus"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("bookStatus"), "%" + commonQuery.get("bookStatus") + "%"));
            }
            // 开始时间
            if (!StringUtils.isEmpty(commonQuery.get("startTime"))) {
                predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("bookDate").as(String.class),
                        String.valueOf(commonQuery.get("startTime"))));
            }
            // 结束时间
            if (!StringUtils.isEmpty(commonQuery.get("endTime"))) {
                predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("bookDate").as(String.class),
                        String.valueOf(commonQuery.get("endTime"))));
            }
            list.add(predicate);

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));

        };
        return bizBookingDao.findAll(specification, pageable);
    }
}
