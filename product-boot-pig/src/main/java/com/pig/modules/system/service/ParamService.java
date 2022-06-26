package com.pig.modules.system.service;

import com.pig.modules.system.entity.Param;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 参数表 服务类
 * </p>
 *
 * @author
 * @since 2020-04-27
 */
public interface ParamService extends IService<Param> {


    String getPath(String key);
}
