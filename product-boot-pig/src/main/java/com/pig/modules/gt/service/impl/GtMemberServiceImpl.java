package com.pig.modules.gt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.modules.gt.entity.GtMember;
import com.pig.modules.gt.mapper.GtMemberMapper;
import com.pig.modules.gt.service.GtMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import java.util.Map;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
@Service
public class GtMemberServiceImpl extends ServiceImpl<GtMemberMapper, GtMember> implements GtMemberService {

    @Autowired
    GtMemberMapper gtMemberMapper;
    @Override
    public GtMember getUserByUserCode(String userCode) {
        QueryWrapper<GtMember> queryWrapper = new QueryWrapper<GtMember>();
        queryWrapper.eq("account",userCode);
        GtMember gtMember = baseMapper.selectOne(queryWrapper);
        return gtMember;
    }

    @Override
    public void doupdateById(Map<String,Object> params) {

        //update
        Date dateNow = new Date();
        gtMemberMapper.doupdateById(params);
    }

    @Override
    public void doinsert(Map<String,Object> params) {
        gtMemberMapper.doinsert(params);
    }

    @Override
    public GtMember selectGtMemberById(String id){

        QueryWrapper<GtMember> queryWrapper = new QueryWrapper<GtMember>();
        queryWrapper.eq("id",id);
        return baseMapper.selectOne(queryWrapper);
    }

}
