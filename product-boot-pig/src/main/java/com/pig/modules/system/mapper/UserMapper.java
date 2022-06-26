package com.pig.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.UserVO;

import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据ID查询一条用户记录
     * @param id
     * @return
     */
    UserVO selectUserVoById(String id);

}
