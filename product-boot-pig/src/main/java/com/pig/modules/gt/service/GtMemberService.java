package com.pig.modules.gt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.modules.gt.entity.GtMember;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface GtMemberService extends IService<GtMember> {

    GtMember getUserByUserCode(String userCode);

    /**
     * 添加/修改用户
     * @return
     */
    void doupdateById(Map<String,Object> params);

    void doinsert(Map<String,Object> params);

    /**
     * 根据ID查询一条用户记录
     * @param id
     * @return
     */
    GtMember selectGtMemberById(String id);



}
