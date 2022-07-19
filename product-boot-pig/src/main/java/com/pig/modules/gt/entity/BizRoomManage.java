package com.pig.modules.gt.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 房间管理(BizRoomManage)实体类
 *
 * @author makejava
 * @since 2022-07-06 22:26:15
 */
@Entity
@Data
public class BizRoomManage extends AbstractBaseTimeEntity implements Serializable {
    private static final long serialVersionUID = -43096791350669929L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 房间名称
     */
    private String name;
    /**
     * 房间类型
     */
    private String roomType;
    /**
     * 1 普通会员，2普通+钻石
     */
    private Integer roleType;
    /**
     * 房间编号1001，1002...，不可重复
     */
    private String roomCode;
    /**
     * 状态，-1可用,0作废
     */
    private String status = "-1";

    @OneToOne
    @JoinColumn(name = "roleType", referencedColumnName = "id", insertable = false, updatable = false)
    private RdRole rdRole;
}

