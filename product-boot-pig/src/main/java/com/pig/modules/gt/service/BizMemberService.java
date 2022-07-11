package com.pig.modules.gt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.modules.gt.entity.BizMember;

/**
 * 用户信息
 * 不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表服务接口
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
public interface BizMemberService extends IService<BizMember> {

}
