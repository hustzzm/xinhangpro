package com.pig.modules.system.entity.DTO;

import lombok.Data;

/**
 * 返回的quartz信息
 */
@Data
public class QuartzJobDTO {

    /**
     * job名称
     */
    private String jobName;
    /**
     * job工作组
     */
    private String jobGroup;

    /**
     * 备注信息
     */
    private String description;

    /**
     * javaClass类
     */
    private String jobClassName;
    /**
     * 上一次触发时间
     */
    private String nextFireTime;
    /**
     * 下次触发时间
     */
    private String prevFireTime;
    /**
     * 优先级
     */
    private Integer priority;

    private String triggerName;

    private String triggerGroup;

    /**
     * 当前状态
     */
    private String triggerState;

    /**
     * 触发器的类型
     */
    private String triggerType;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * cron表达式
     */
    private String cronExpression;
}
