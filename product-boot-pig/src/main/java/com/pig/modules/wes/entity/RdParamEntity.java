package com.pig.modules.wes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 参数表
 *
 * @author gaby
 * @email 534139685@qq.com
 * @date 2021-10-26 15:48:39
 */
@Data
@TableName("rd_param")
public class RdParamEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 参数名
     */
    @TableField("param_name")
    private String paramName;
    /**
     * 参数键
     */
    @TableField("param_key")
    private String paramKey;
    /**
     * 参数值
     */
    @TableField("param_value")
    private String paramValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改人
     */
    @TableField("update_user")
    private Long updateUser;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否已删除
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 表名
     */
    public static final String TABLE_NAME = "rd_param" ;
    /**
     * 主键
     */
    public static final String ID = "id" ;

    /**
     * 参数名
     */
    public static final String PARAM_NAME = "param_name" ;

    /**
     * 参数键
     */
    public static final String PARAM_KEY = "param_key" ;

    /**
     * 参数值
     */
    public static final String PARAM_VALUE = "param_value" ;

    /**
     * 备注
     */
    public static final String REMARK = "remark" ;

    /**
     * 创建人
     */
    public static final String CREATE_USER = "create_user" ;

    /**
     * 创建时间
     */
    public static final String CREATE_TIME = "create_time" ;

    /**
     * 修改人
     */
    public static final String UPDATE_USER = "update_user" ;

    /**
     * 修改时间
     */
    public static final String UPDATE_TIME = "update_time" ;

    /**
     * 状态
     */
    public static final String STATUS = "status" ;

    /**
     * 是否已删除
     */
    public static final String IS_DELETED = "is_deleted" ;


}
