package com.pig.modules.gt.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;

/**
 * (BizBooking)实体类
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
@Entity
@Data
public class BizBooking extends AbstractBaseTimeEntity implements Serializable {
    private static final long serialVersionUID = -96491309107003392L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 预订编号:B1HC5M1ND5g
     * B+时间戳转32进制+3位随机数
     * 需按照普通会员、钻石会员预订规则
     */
    private String booksNo;
    /**
     * 微信openid，个人公开信息获得
     */
    private String openid;
    /**
     * 微信号
     */
    private String account;
    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 预订时间：9-20;
     * 9：9:00-10:00;
     * 10：10:00-11:00;
     * 11：11:00-12:00;
     * 12：12:00-13:00;
     * 13：13:00-14:00;
     * 14：14:00-15:00;
     * 15：15:00-16:00;
     * 16：16:00-17:00;
     * 17：17:00-18:00;
     * 18：18:00-19:00;
     * 19：19:00-20:00;
     * 20：20:00-21:00;
     */
    private String bookTimes;
    /**
     * 房间编号
     */
    private String roomCode;
    /**
     * 预订日期
     */
    private Date bookDate;
    /**
     * 预约状态1 预约成功，3消费完成，5取消预约
     */
    private String bookStatus;
    /**
     * 状态，-1可用,0作废
     */
    private String status = "-1";
    /**
     * 房间类型
     */
    private String roomType;
    /**
     * 房间名称
     */
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "openid", referencedColumnName = "openid", insertable = false, updatable = false)
    private BizMember bizMember;
}

