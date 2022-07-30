package com.pig.modules.gt.service.impl;

import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.StringUtil;
import com.pig.basic.util.utils.DateUtils;
import com.pig.modules.gt.constant.HomeEnum;
import com.pig.modules.gt.dao.BizMemberDao;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private BizMemberDao bizMemberDao;

    @Resource
    private EntityManager entityManager;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Page<BizOrder> page(Map<String, Object> params) {
        //分页，以及排序方式
        //注意点：pageNo 是从0开始的，pageSize是当前页查询个数
        CommonQuery commonQuery = new CommonQuery(params);
        Pageable pageable = PageRequest.of(commonQuery.getCurrent() - 1, commonQuery.getSize(), Sort.by(Sort.Direction.DESC, "createTime"));
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
                predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(String.class), String.valueOf(commonQuery.get("startTime"))));
            }
            // 结束时间
            if (!StringUtils.isEmpty(commonQuery.get("endTime"))) {
                predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(String.class), String.valueOf(commonQuery.get("endTime"))));
            }
            //status =-1为可用
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("status"), "-1"));
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
            String orderName =
                    order.getRdRole().getRoleName() + " " + sdf.format(order.getOrderStart()) + "至" + sdf.format(order.getOrderEnd());
            String orderStatus = HomeEnum.CommonEnum.getValue(order.getOrderStatus());
            BizOrderExportVO orderExportVO = BizOrderExportVO.builder().orderNo(order.getOrderNo()).orderAccount(order.getOrderAccount()).createTime(order.getCreateTime()).orderName(orderName).orderPrice(String.valueOf(order.getOrderPrice())).orderStatus(orderStatus).build();
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

    @Override
    public CommonResult insert(Map<String, Object> params) {
        String msg = checkParam(params);
        if (!StringUtils.isEmpty(msg)) {
            return CommonResult.failed(msg);
        }
        // 开始逻辑处理
        String openId = StringUtil.getCheckString(params.get("openId"));
        BizOrder bizOrder = orderDao.findByOpenIdAndNoPay(openId);
        // 如果订单已存在
        if (null != bizOrder && StringUtil.isNull(bizOrder.getOrderStatus())) {
            // 1 检查是否有未处理的订单，如果有则返回，不执行添加操作；
//            if (bizOrder.getOrderStatus().equalsIgnoreCase(HomeEnum.CommonEnum.TO_BE_PAID.getKey())) {
//
//            }
            return CommonResult.failed("已有未付款的订单，请进入我的订单中进行付款操作！");

        }
        String dateNow = sdf.format(new Date());
        bizOrder = orderDao.findByOpenIdAndNoExpired(openId, dateNow);
        // 2 检查是否有已经购买且未过期的订单，如果有则返回，不执行添加操作；
        if (bizOrder != null && null != bizOrder.getOrderEnd()) {
//            Date now = new Date();
//            Date orderEnd = bizOrder.getOrderEnd();
//            // 如果当前时间小于过期时间，则不添加
//            if (now.compareTo(orderEnd) < 0) {
//
//            }
            return CommonResult.failed("当前会员未过期，无需重复购买！");
        }

        bizOrder = new BizOrder();
        // 新增订单
        String orderNo = UUID.randomUUID().toString(); // 订单号
        String orderType = StringUtil.getCheckString(params.get("orderType"));
        Double orderPrice = StringUtil.getCheckDouble(params.get("orderPrice"));
        String userLevel = StringUtil.getCheckString(params.get("userLevel"));

        bizOrder.setOpenId(openId);
        bizOrder.setOrderNo(orderNo);
        bizOrder.setOrderType(orderType);
        bizOrder.setOrderPrice(orderPrice);
        bizOrder.setUserLevel(userLevel);

        orderDao.save(bizOrder);
        // orderNo更新至member表中
        bizMemberDao.updateOrderNoByOpenId(openId, orderNo);
        return CommonResult.ok(bizOrder);
    }

    /**
     * 参数检查
     *
     * @param params
     */
    private String checkParam(Map<String, Object> params) {
        String msg = "";
        if (StringUtil.isNull(params.get("openId"))) {
            return "openId不能为空！";
        }
        if (StringUtil.isNull(params.get("orderType"))) {
            return "会员类型不能为空！";
        }
        if (StringUtil.isNull(params.get("orderPrice"))) {
            return "付款金额不能为空！";
        }
        if (StringUtil.isNull(params.get("userLevel"))) {
            return "会员等级不能为空！";
        }
        return msg;
    }
}
