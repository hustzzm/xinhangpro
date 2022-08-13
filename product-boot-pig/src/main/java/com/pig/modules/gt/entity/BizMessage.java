package com.pig.modules.gt.entity;

import com.google.gson.annotations.JsonAdapter;
import com.pig.basic.adapter.DateTimeAdapter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * (BizMessage)实体类
 *
 * @author makejava
 * @since 2022-07-10 20:26:33
 */
@Entity
@Data

public class BizMessage extends AbstractBaseTimeEntity implements Serializable {
    private static final long serialVersionUID = -96491309107003392L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 微信openid，个人公开信息获得
     */
    private String openid;
    /**
     * 姓名
     */
    private String message;
    /**
     * 发送消息时间
     */
    private String sendtime;
    /**
     * 续费状态 1 未续费，3，已续费，5已过期
     */
    private String finishstate;

    /**
     * 状态，-1可用,0作废
     */
    private String status = "-1";
}

