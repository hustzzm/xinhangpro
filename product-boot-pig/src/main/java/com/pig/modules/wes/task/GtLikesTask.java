package com.pig.modules.wes.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.modules.gt.entity.GtMember;
import com.pig.modules.gt.mapper.GtMemberMapper;
import com.pig.modules.system.mapper.ParamMapper;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 定时任务:点赞
 * 调度频率 每2小时一次
 * update by hello
 * 2022-4-30
 */
@Log4j2
@DisallowConcurrentExecution
@Component
public class GtLikesTask implements Job {

    @Autowired
    private ParamMapper paramMapper;
    @Autowired
    private GtMemberMapper gtMemberMapper;
    protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {

            QueryWrapper<GtMember> queryWrapper = new QueryWrapper<GtMember>();
            queryWrapper.eq("is_deleted", 0);
            queryWrapper.eq("role_id", 1);

            Date dateTimeNow = new Date();

            List<GtMember> memberList = gtMemberMapper.selectList(queryWrapper);

            log.info("rd-wes do GtLikesTask start ---- {} ----", df.format(new Date()));

        } catch (Exception ex) {
            log.info("rd-wes do GtLikesTask error : {}", ex.toString());
        }finally {
            log.info("rd-wes do GtLikesTask end ---- {} ----", df.format(new Date()));
        }
    }


    /**
     * 初始化配置参数
     */
    private void initParamConfig(){

        //获取路径参数
//        QueryWrapper<Param> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("status",1);
//        queryWrapper.eq("param_key","wes_cifs_path");
//        Param paramMap = paramMapper.selectOne(queryWrapper);
//        wes_cifs_path = paramMap.getParamValue();

//        queryWrapper.clear();
//        queryWrapper.eq("status",1);
//        queryWrapper.eq("param_key","wes_fastq_path");
//        paramMap = paramMapper.selectOne(queryWrapper);
//        wes_fastq_path = paramMap.getParamValue();
//
//        queryWrapper.clear();
//        queryWrapper.eq("status",1);
//        queryWrapper.eq("param_key","wes_result_path");
//        paramMap = paramMapper.selectOne(queryWrapper);
//        wes_result_path = paramMap.getParamValue();
//
//        queryWrapper.clear();
//        queryWrapper.eq("status",1);
//        queryWrapper.eq("param_key","wes_server_api");
//        paramMap = paramMapper.selectOne(queryWrapper);
//        wes_server_api = paramMap.getParamValue();

    }

}
