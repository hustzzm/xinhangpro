package com.pig.modules.gt.service.impl;

import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.utils.JpaUtil;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizCompany;
import com.pig.modules.gt.dao.BizCompanyDao;
import com.pig.modules.gt.entity.BizRoomManage;
import com.pig.modules.gt.service.BizCompanyService;
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
 * 房间管理(BizCompany)表服务实现类
 *
 * @author makejava
 * @since 2022-07-12 23:32:41
 */
@Service("bizCompanyService")
public class BizCompanyServiceImpl implements BizCompanyService {
    @Resource
    private BizCompanyDao bizCompanyDao;

    @Override
    public Page<BizCompany> page(Map<String, Object> params) {
        //分页，以及排序方式
        //注意点：pageNo 是从0开始的，pageSize是当前页查询个数
        CommonQuery commonQuery = new CommonQuery(params);
        Pageable pageable = PageRequest.of(commonQuery.getCurrent() - 1, commonQuery.getSize(),
                Sort.by(Sort.Direction.ASC, "createTime"));
        Specification<BizCompany> specification = (root, criteriaQuery, criteriaBuilder) -> {
            //增加筛选条件
            // 房间名
            Predicate predicate = criteriaBuilder.conjunction();
            if (!StringUtils.isEmpty(commonQuery.get("compname"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("compname"), "%" + commonQuery.get("compname") + "%"));
            }
            return predicate;
        };
        return bizCompanyDao.findAll(specification, pageable);
    }

    @Override
    public void update(BizCompany bizCompany) {
        if (bizCompany != null && bizCompany.getId() != null) {
            BizCompany bizCompanyBak = bizCompanyDao.getOne(bizCompany.getId());
            if (bizCompanyBak != null) {
                JpaUtil.copyNotNullProperties(bizCompany, bizCompanyBak);
            }
            bizCompanyDao.save(bizCompanyBak);
        }
    }
}
