package com.pig.modules.gt.entity;

import com.google.gson.annotations.JsonAdapter;
import com.pig.basic.adapter.DateTimeAdapter;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 自动更新创建时间和更新时间
 */
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
@Data
public abstract class AbstractBaseTimeEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonAdapter(DateTimeAdapter.class)
    private Date createTime;

    @LastModifiedDate
    @JsonAdapter(DateTimeAdapter.class)
    private Date updateTime;

    @Column(name = "create_by", length = 50)
    @CreatedBy
    private String createBy;

    @Column(name = "update_by", length = 50)
    @LastModifiedBy
    private String updatedBy;
}
