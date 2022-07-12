package com.pig.modules.gt.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class BizCompany extends AbstractBaseTimeEntity implements Serializable {
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
    private String comptype;
    private String compname;
    private String compaddr;
    private String telphone;
    private String shopfee;
    private String toppig;
    private String details;
    private String subpig;
    private String remark;


    /**
     * 状态，-1可用,1拉黑，0作废
     */
    private Integer status;
}

