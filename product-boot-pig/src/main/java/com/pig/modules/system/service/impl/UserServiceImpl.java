package com.pig.modules.system.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.basic.constant.SystemConstant;
import com.pig.basic.exception.ServiceException;
import com.pig.basic.util.CommonUtil;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.UserVO;
import com.pig.modules.system.mapper.UserMapper;
import com.pig.modules.system.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserByUserName(String userCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("account",userCode);
        User user = baseMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public boolean submit(User user) {
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("account",user.getAccount());
        Integer cnt = baseMapper.selectCount(queryWrapper);
        if (cnt > 0) {
            throw new ApiException("当前用户已存在!");
        }
        return saveOrUpdate(user);
    }

    @Override
    public UserVO selectUserVoById(String id){
        return baseMapper.selectUserVoById(id);
    }

    @Override
    public boolean grantRole(String userIds, String roleIds) {
        User user = new User();
        user.setRoleId(roleIds);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.in("id", CommonUtil.toLongList(userIds));
        return this.update(user,queryWrapper);
    }

    @Override
    public boolean resetPassword(String userIds) {
        User user = new User();
        user.setPassword(DigestUtil.md5Hex(SystemConstant.DEFAULT_PASSWORD));
        user.setUpdateTime(new Date());
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.in("id", CommonUtil.toLongList(userIds));
        return this.update(user,queryWrapper);
    }

    @Override
    public boolean updatePassword(String userId, String oldPassword, String newPassword, String newPassword1) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("account", userId);
        User one = getOne(queryWrapper);

        if (!newPassword.equals(newPassword1)) {
            throw new ServiceException("请输入正确的确认密码!");
        }
        if (!one.getPassword().equals(DigestUtil.md5Hex(oldPassword))) {
            throw new ServiceException("原密码不正确!");
        }
        return this.update(Wrappers.<User>update().lambda().set(User::getPassword, DigestUtil.md5Hex(newPassword)).eq(User::getId, one.getId()));
    }
}
