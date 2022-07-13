package com.pig.modules.gt.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 商家管理
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
     * 商家类型，见字典
     */
    private Integer comptype;
    /**
     * 商家名称
     */
    private String compname;
    /**
     * 商家地址
     */
    private String compaddr;
    /**
     * 商家联系电话
     */
    private String telphone;
    /**
     * 消费折扣：0.65，表示6.5折
     */
    private Double shopfee;
    /**
     * 列表展示logo，连接地址 只允许一张
     */
    private String toppig;
    /**
     * 详细文本内容
     */
    private String details;
    /**
     * 其他图片，只允许一张
     */
    private String subpig;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态，-1可用 其他不可用
     */
    private Integer status;
    /**
     * 详细页面图片，只允许一张
     */
    private String subheadpig;
}

