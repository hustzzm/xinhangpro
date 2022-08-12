package com.pig.modules.gt.service;

import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.entity.BizMemberVO;
import org.springframework.data.domain.Page;

import java.util.List;
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

    /**
     * 根据openId查询个人生效期内的会员记录
     * @return
     */
    BizMember findExistRecord(String openid, String endDate);

    CommonResult insertOrUpdate(Map<String, Object> params);

    CommonResult updateStatus(Map<String, Object> params);

    void exportData(ScrollResultsHandler<BizMemberVO> scrollResultsHandler);
}
