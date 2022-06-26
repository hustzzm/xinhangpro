package com.pig.basic.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

@Service
public class ShiroService {

    @Autowired
    private UserMapper userMapper;

    public User selectUserByCode(String userCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("account",userCode);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    public User getUserInfo(String userCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("account",userCode);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }
}
