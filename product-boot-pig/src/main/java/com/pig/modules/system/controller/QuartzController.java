package com.pig.modules.system.controller;

import com.pig.basic.adapter.DateTimeAdapter;
import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.modules.system.entity.DTO.QuartzJobDTO;
import com.pig.modules.system.entity.vo.JobVo;
import com.pig.modules.system.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 定时任务处理器
 */
@RestController
@RequestMapping("/quartz")
public class QuartzController {

    @Autowired
    private JobService jobService;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @GetMapping(value = "/page",produces = "application/json;charset=utf-8")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        CommonQuery query = new CommonQuery(params);
        return jobService.queryList(query);
    }

    /**
     * 添加job
     *
     * @param jobVo
     * @return
     */
    @PostMapping(value = "/addJob")
    public CommonResult addJob(@RequestBody JobVo jobVo) {
        boolean addJob = jobService.addJob(jobVo);
        if(addJob){
            return CommonResult.ok("添加任务成功");
        }else{
            return CommonResult.failed("添加任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param jobVo
     * @return
     */
    @PostMapping(value = "/resumeJob")
    public CommonResult resumeJob(QuartzJobDTO jobVo) {

        //modify by hello 20210830
        //如果下次执行时间小于当前时间 + 2分钟，则更新下次执行时间
        Date nowDate = new Date();
        nowDate.setTime(nowDate.getTime() + 2*60*1000);
        try {
            String nextFireTime = jobVo.getNextFireTime();
            boolean bpass = sdf.parse(nextFireTime).before(nowDate);
            if (bpass) {
                String dateNow = String.valueOf(nowDate.getTime());
                jobVo.setNextFireTime(dateNow);
                jobService.updateJobNextTime(jobVo);
                boolean addJob = jobService.resumeJob(jobVo);

                if (addJob) {
                    return CommonResult.ok("恢复任务成功");
                } else {
                    return CommonResult.failed("恢复任务失败");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CommonResult.failed("恢复任务失败");
    }

    /**
     * 暂停任务
     *
     * @param jobVo
     * @return
     */
    @PostMapping(value = "/pauseJob")
    public CommonResult pauseJob(QuartzJobDTO jobVo) {
        boolean addJob = jobService.pauseJob(jobVo);
        if(addJob){
            return CommonResult.ok("暂停任务成功");
        }else{
            return CommonResult.failed("暂停任务失败");
        }
    }

    /**
     * 删除任务
     *
     * @param jobVo
     * @return
     */
    @PostMapping(value = "/removeJob")
    public CommonResult removeJob(QuartzJobDTO jobVo) {
        boolean addJob = jobService.removeJob(jobVo);
        if(addJob){
            return CommonResult.ok("删除任务成功");
        }else{
            return CommonResult.failed("删除任务失败");
        }
    }
}
