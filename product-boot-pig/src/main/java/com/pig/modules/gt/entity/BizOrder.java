package com.pig.modules.gt.entity;

import com.google.gson.annotations.JsonAdapter;
import com.pig.basic.adapter.DateTimeAdapter;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单信息表(BizOrder)实体类
 *
 * @author makejava
 * @since 2022-07-09 22:19:14
 */
@Entity
@Data
public class BizOrder extends AbstractBaseTimeEntity implements Serializable {
    private static final long serialVersionUID = -19088977913835102L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单用户微信号
     */
    private String orderAccount;
    /**
     * 微信昵称
     */
    private String nickName;

    private String openId;
    /**
     * 订单状态 5  待付款  10 已支付  20 支付失败
     */
    private String orderStatus = "5";
    /**
     * 订单类型 1-6
     */
    private String orderType = "5";
    /**
     * 订单金额
     */
    private Double orderPrice;
    /**
     * 会员类型
     */
    private String userLevel;
    /**
     * 订单生效日期-开始
     */
    private Date orderStart;
    /**
     * 订单生效日期-结束
     */
    private Date orderEnd;
    /**
     * 付款金额
     */
    private Double payPrice;
    /**
     * 付款时间
     */
    private String payTime;

    private Date deletedAt;

    /**
     * 状态，-1可用,0作废
     */
    private String status = "-1";

    @OneToOne
    @JoinColumn(name = "userLevel", referencedColumnName = "id", insertable = false, updatable = false)
    private RdRole rdRole;
}

