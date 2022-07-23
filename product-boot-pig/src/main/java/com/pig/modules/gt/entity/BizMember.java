package com.pig.modules.gt.entity;

import com.google.gson.annotations.JsonAdapter;
import com.pig.basic.adapter.DateTimeAdapter;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * 不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)实体类
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
@Entity
@Data
public class BizMember extends AbstractBaseTimeEntity implements Serializable {
    private static final long serialVersionUID = -91245255578607013L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 微信号，唯一性校验
     */
    private String account;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 备用
     */
    private String password;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户年龄
     */
    private String age;
    /**
     * 性别1 男，2 女，3未知
     */
    private String gender;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 微信openid，个人公开信息获得
     */
    private String openid;
    /**
     * 添加日期
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonAdapter(DateTimeAdapter.class)
    private Date registerTime;
    /**
     * 会员类型：0 非会员，1 普通会员，2钻石会员
     */
    private String userLevel = "0";
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 微信图片地址
     */
    private String avatar;
    /**
     * 本人正脸照片图片地址
     */
    private String selfpig;
    /**
     * 驾驶证正面图片地址
     */
    private String pigjs1;
    /**
     * 驾驶证反面图片地址
     */
    private String pigjs2;
    /**
     * 行驶证正面图片地址
     */
    private String pigxs1;
    /**
     * 行驶证反面图片地址
     */
    private String pigxs2;
    /**
     * 身份证正面图片地址
     */
    private String pigsf1;
    /**
     * 身份证反面图片地址
     */
    private String pigsf2;
    /**
     * 会员到期时间
     */
    private String endDate;
    /**
     * 状态，-1可用,1拉黑，0作废
     */
    private String status = "-1";
}

