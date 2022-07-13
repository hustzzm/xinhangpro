package com.pig.modules.gt.service.impl;

import com.pig.basic.util.CommonQuery;
import com.pig.modules.gt.constant.HomeEnum;
import com.pig.modules.gt.dao.BizOrderDao;
import com.pig.modules.gt.entity.BizOrder;
import com.pig.modules.gt.entity.BizOrderExportVO;
import com.pig.modules.gt.service.BizOrderService;
import com.pig.modules.gt.service.ScrollResultsHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;

/**
 * 订单信息表(BizOrder)表服务实现类
 *
 * @author makejava
 * @since 2022-07-09 22:19:14
 */
@Service("bizOrderService")
public class BizOrderServiceImpl implements BizOrderService {
    @Resource
    private BizOrderDao orderDao;

    @Resource
    private EntityManager entityManager;


    @Override
    public Page<BizOrder> page(Map<String, Object> params) {
        //分页，以及排序方式
        //注意点：pageNo 是从0开始的，pageSize是当前页查询个数
        CommonQuery commonQuery = new CommonQuery(params);
        Pageable pageable = PageRequest.of(commonQuery.getCurrent() - 1, commonQuery.getSize(),
                Sort.by(Sort.Direction.ASC, "createTime"));
        Specification<BizOrder> specification = getBizOrderSpecification(commonQuery);

        return orderDao.findAll(specification, pageable);
    }

    private Specification<BizOrder> getBizOrderSpecification(CommonQuery commonQuery) {
        Specification<BizOrder> specification = (root, criteriaQuery, criteriaBuilder) -> {
            //增加筛选条件
            Predicate predicate = criteriaBuilder.conjunction();
            //name不为空
            if (!StringUtils.isEmpty(commonQuery.get("openId"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("openId"), "%" + commonQuery.get("openId") + "%"));
            }
            if (!StringUtils.isEmpty(commonQuery.get("orderStatus"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("orderStatus"), "%" + commonQuery.get("orderStatus") + "%"));
            }
            //name不为空
            if (!StringUtils.isEmpty(commonQuery.get("orderNo"))) {
                predicate.getExpressions().add(root.get("orderNo"));
            }
            if (!StringUtils.isEmpty(commonQuery.get("nickName"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("nickName"), "%" + commonQuery.get("nickName") + "%"));
            }
            if (!StringUtils.isEmpty(commonQuery.get("userLevel"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("userLevel"), "%" + commonQuery.get("userLevel") + "%"));
            }
            // 开始时间
            if (!StringUtils.isEmpty(commonQuery.get("startTime"))) {
                predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(String.class),
                        String.valueOf(commonQuery.get("startTime"))));
            }
            // 结束时间
            if (!StringUtils.isEmpty(commonQuery.get("endTime"))) {
                predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(String.class),
                        String.valueOf(commonQuery.get("endTime"))));
            }
            return predicate;
        };
        return specification;
    }

    /**
     * 需要加事务注解，并且是只读事务
     * <p>
     * 需要及时调用entityManager的detach方法释放内存，不然还是会出现OOM
     *
     * @param scrollResultsHandler scrollResultsHandler
     */
    @Override
    @Transactional(readOnly = true)
    public void exportData(ScrollResultsHandler<BizOrderExportVO> scrollResultsHandler) {
        List<BizOrder> all = orderDao.findAll();
        all.stream().forEach((order) -> {
            String orderName = order.getRdRole().getRoleName() + " " + order.getOrderStart() + " " + order.getOrderEnd();
            String orderStatus = HomeEnum.CommonEnum.getValue(order.getOrderStatus());
            BizOrderExportVO orderExportVO = BizOrderExportVO.builder()
                    .orderNo(order.getOrderNo())
                    .orderAccount(order.getOrderAccount())
                    .createTime(order.getCreateTime())
                    .orderName(orderName)
                    .orderPrice(String.valueOf(order.getOrderPrice()))
                    .orderStatus(orderStatus)
                    .build();
            scrollResultsHandler.handle(orderExportVO);

            //对象被session持有，调用detach方法释放内存
            entityManager.detach(order);
        });
    }

    @Override
    public double getTotalAmount(Map<String, Object> params) {
        CommonQuery commonQuery = new CommonQuery(params);
        Specification<BizOrder> specification = getBizOrderSpecification(commonQuery);
        // 按条件计算总金额
        List<BizOrder> orderList = orderDao.findAll(specification);
        return orderList.stream().mapToDouble(BizOrder::getOrderPrice).sum();
    }
}
