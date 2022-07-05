package com.pig.modules.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.beans.Transient;
import java.util.Date;
import java.io.Serializable;

/**
 * 房间管理(RoomManage)实体类
 *
 * @author makejava
 * @since 2022-07-05 22:40:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("biz_room_manage")
public class RoomManage implements Serializable {
    private static final long serialVersionUID = 905556148616233395L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 1 普通会员，2普通+钻石
     */
    @TableField(exist = false)
    private String roleName;

    /**
     * 创建者
     */
    private String createBy;
    /**
     * 修改者
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}

