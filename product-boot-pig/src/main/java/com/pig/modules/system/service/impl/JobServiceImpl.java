package com.pig.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.modules.system.entity.DTO.QuartzJobDTO;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.JobVo;
import com.pig.modules.system.mapper.QuartzMapper;
import com.pig.modules.system.service.JobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 定时任务服务实现
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzMapper quartzMapper;

    @Override
    public boolean addJob(JobVo jobVo) {
        try {
            //构建job信息
            Class cls = Class.forName(jobVo.getClassName()) ;
            // cls.newInstance(); // 检验类是否存在
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobVo.getJobName(),jobVo.getJobGroup())
                    .withDescription(jobVo.getDesc()).build();

            // 触发时间点
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobVo.getCron().trim());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + jobVo.getJobName(), jobVo.getJobGroup())
                    .startNow().withSchedule(cronScheduleBuilder).build();
            //交由Scheduler安排触发
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CommonResult queryList(CommonQuery query) {

        QueryWrapper queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(query.get("jobName"))){
            queryWrapper.like("qjd.JOB_NAME", "%" + query.get("jobName") + "%");
        }
        Page pageBP = new Page(query.getCurrent(),query.getSize());
        IPage<QuartzJobDTO> quartzJobDTOIPage = quartzMapper.selectQuartzPage(pageBP, queryWrapper);
        return CommonResult.ok(quartzJobDTOIPage);
    }

    @Override
    public boolean resumeJob(QuartzJobDTO jobVo) {
        boolean flag = true;
        JobKey jobKey = JobKey.jobKey(jobVo.getJobName(), jobVo.getJobGroup());
        try {
            scheduler.resumeJob(jobKey);
            flag = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
    @Override
    public void updateJobNextTime(QuartzJobDTO jobVo) {
        quartzMapper.updateJobNextTime(jobVo);
    }

    @Override
    public boolean pauseJob(QuartzJobDTO jobVo) {
        boolean flag = true;
        JobKey jobKey = JobKey.jobKey(jobVo.getJobName(), jobVo.getJobGroup());
        try {
            scheduler.pauseJob(jobKey);
            flag = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean removeJob(QuartzJobDTO jobVo) {
        boolean flag = true;
        JobKey jobKey = JobKey.jobKey(jobVo.getJobName(), jobVo.getJobGroup());
        try {
            scheduler.deleteJob(jobKey);
            flag = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
