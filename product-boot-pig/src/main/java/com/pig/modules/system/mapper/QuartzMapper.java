package com.pig.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pig.modules.system.entity.DTO.QuartzJobDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuartzMapper {

    /**
     * 查询定时任务列表信息
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<QuartzJobDTO> selectQuartzPage(IPage page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);


    /**
     * 修改下次执行时间
     * @param quartzJobDTO
     */
    void updateJobNextTime(@Param("params") QuartzJobDTO quartzJobDTO);
}