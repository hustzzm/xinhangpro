package com.pig.modules.wes.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.modules.system.entity.Param;
import com.pig.modules.system.mapper.ParamMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    /**
     * 同步调用命令，主线程会阻塞
     * @param command
     */
    public static void executeSync(String command){
        Runtime r = Runtime.getRuntime();
        String[] cmd = new String[] { "/bin/sh", "-c", command };
        Process p = null;
        long start = System.currentTimeMillis();
        try {
            p = r.exec(cmd);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long end = System.currentTimeMillis();
            logger.info("CMD time:"+ (end - start) );
            logger.info(StringUtils.join(cmd," "));
        }

    }

    /**
     * 获取数据配置
     * @param paramString
     * @return
     */
    public static String getParam(String paramString){
        if(CacheUtil.isExist(paramString)){
            return (String) CacheUtil.get(paramString);
        }
        ParamMapper paramMapper = SpringContextUtil.getBean(ParamMapper.class);
        //获取路径参数
        QueryWrapper<Param> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        queryWrapper.eq("param_key",paramString);
        List<Param> params = paramMapper.selectList(queryWrapper);
        if(params.size() > 0){
            Param paramMap = params.get(0);
            String paramValue = paramMap.getParamValue();
            String value = paramValue == null ? "" : paramValue;
            CacheUtil.set(paramString,value);
            return paramValue;
        }
        return "";
    }
}
