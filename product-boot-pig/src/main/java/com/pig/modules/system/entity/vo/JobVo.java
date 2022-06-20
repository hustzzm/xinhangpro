package com.pig.modules.system.entity.vo;

import lombok.Data;

/**
 * 前端传的json串数据
 */
@Data
public class JobVo {

    /**
     * 名称
     */
    private String jobName;

    /**
     * 类名
     */
    private String className;
    /**
     * 时间表达式
     */
    private String cron;

    /**
     * 任务描述
     */
    private String desc;

    /**
     * 工作组
     */
    private String jobGroup;
}
