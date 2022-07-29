package com.pig.modules.gt.service;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.entity.BizMemberVO;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * 用户信息
 * 不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表服务接口
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
public interface BizMemberService {

    BizMember findByOpenidAndStatus(String openid, String status);

    Page<BizMember> page(Map<String, Object> params);

    CommonResult insertOrUpdate(Map<String, Object> params);

    CommonResult updateStatus(Map<String, Object> params);

    void exportData(ScrollResultsHandler<BizMemberVO> scrollResultsHandler);
}
