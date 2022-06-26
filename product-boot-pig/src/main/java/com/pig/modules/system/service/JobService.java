package com.pig.modules.system.service;

import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.modules.system.entity.DTO.QuartzJobDTO;
import com.pig.modules.system.entity.vo.JobVo;

/**
 * 定时任务服务类
 */
public interface JobService {

    /**
     * 添加定时任务
     */
    boolean addJob(JobVo jobVo);

    /**
     * 查询定时任务列表信息
     * @param query
     * @return
     */
    CommonResult queryList(CommonQuery query);

    /**
     * 恢复任务
     * @param jobVo
     * @return
     */
    boolean resumeJob(QuartzJobDTO jobVo);

    /**
     * 恢复任务
     * @param jobVo
     * @return
     */
    void updateJobNextTime(QuartzJobDTO jobVo);

    /**
     * 暂停任务
     * @param jobVo
     * @return
     */
    boolean pauseJob(QuartzJobDTO jobVo);

    /**
     * 移除任务
     * @param jobVo
     * @return
     */
    boolean removeJob(QuartzJobDTO jobVo);
}
