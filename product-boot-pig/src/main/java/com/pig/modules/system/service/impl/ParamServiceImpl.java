package com.pig.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.modules.system.entity.Param;
import com.pig.modules.system.mapper.ParamMapper;
import com.pig.modules.system.service.ParamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-04-27
 */
@Service
public class ParamServiceImpl extends ServiceImpl<ParamMapper, Param> implements ParamService {

    @Override
    public String getPath(String key) {
        QueryWrapper<Param> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.eq("status", 1);
        queryWrapper.eq("param_key", key);
        Param param = baseMapper.selectOne(queryWrapper);
        return param.getParamValue();
    }
}
