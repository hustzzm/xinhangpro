package com.pig.modules.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 角色表(RdRole)实体类
 *
 * @author makejava
 * @since 2022-07-05 23:28:07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rd_role")
public class RdRole implements Serializable {
    private static final long serialVersionUID = 896390784137960441L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 父主键
     */
    private Long parentId;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 角色别名
     */
    private String roleAlias;
    /**
     * 是否已删除
     */
    private Integer isDeleted;
}

