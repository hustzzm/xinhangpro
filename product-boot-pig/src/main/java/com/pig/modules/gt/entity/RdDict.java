package com.pig.modules.gt.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 字典表(RdDict)实体类
 *
 * @author makejava
 * @since 2022-07-25 21:53:55
 */
@Entity
@Data
public class RdDict implements Serializable {
    private static final long serialVersionUID = -54043551037958261L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 父主键
     */
    private String parentId;
    /**
     * 字典码
     */
    private String code;
    /**
     * 字典值
     */
    private String dictKey;
    /**
     * 字典名称
     */
    private String dictValue;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 字典备注
     */
    private String remark;
    /**
     * 是否已删除
     */
    private String isDeleted;

}

