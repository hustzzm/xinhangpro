package com.pig.modules.gt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig.basic.util.CommonQuery;
import com.pig.basic.util.CommonResult;
import com.pig.basic.util.DateUtil;
import com.pig.basic.util.StringUtil;
import com.pig.modules.gt.dao.BizMemberDao;
import com.pig.modules.gt.entity.BizCompany;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.service.BizMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.Map;
import java.util.Optional;

/**
 * 用户信息
 * 不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表服务实现类
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
@Service("bizMemberService")
@Slf4j
public class BizMemberServiceImpl implements BizMemberService {
    @Resource
    private BizMemberDao memberDao;

    @Override
    public BizMember findByOpenidAndStatus(String openid, String status){

        return memberDao.findByOpenidAndStatus(openid,status);
    }

    @Override
    public Page<BizMember> page(Map<String, Object> params){

        CommonQuery commonQuery = new CommonQuery(params);
        Pageable pageable = PageRequest.of(commonQuery.getCurrent() - 1, commonQuery.getSize(),
                Sort.by(Sort.Direction.ASC, "createTime"));
        Specification<BizMember> specification = (root, criteriaQuery, criteriaBuilder) -> {
            //增加筛选条件
            // 房间名
            Predicate predicate = criteriaBuilder.conjunction();
            if (!StringUtils.isEmpty(commonQuery.get("name"))) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + commonQuery.get("name") + "%"));
            }
            return predicate;
        };
        return memberDao.findAll(specification, pageable);
    }

    @Override
    public CommonResult insertOrUpdate(Map<String, Object> params) {
        log.info("bizMemberService.insertOrUpdate.params={}", params);
        BizMember bizMember = null;
        Optional<BizMember> memberOptional = memberDao.findById(StringUtil.getCheckInteger(params.get("id")));
        if (memberOptional.isPresent()) {
            bizMember = memberOptional.get();
        } else {
            bizMember = new BizMember();
        }
        if (!StringUtil.isNull(params.get("account"))) {
            bizMember.setAccount(StringUtil.getCheckString(params.get("account")));
        }
        if (!StringUtil.isNull(params.get("password"))) {
            bizMember.setPassword(StringUtil.getCheckString(params.get("password")));
        }
        if (!StringUtil.isNull(params.get("name"))) {
            bizMember.setName(StringUtil.getCheckString(params.get("name")));
        }
        if (!StringUtil.isNull(params.get("gender"))) {
            bizMember.setGender(StringUtil.getCheckString(params.get("gender")));
        }
        if (!StringUtil.isNull(params.get("birthday"))) {
            bizMember.setBirthday(StringUtil.getCheckString(params.get("birthday")));
        }
        if (!StringUtil.isNull(params.get("openid"))) {
            bizMember.setOpenid(StringUtil.getCheckString(params.get("openid")));
        }
        if (!StringUtil.isNull(params.get("userLevel"))) {
            bizMember.setUserLevel(StringUtil.getCheckString(params.get("userLevel")));
        }
        if (!StringUtil.isNull(params.get("nickname"))) {
            bizMember.setNickname(StringUtil.getCheckString(params.get("nickname")));
        }
        if (!StringUtil.isNull(params.get("mobile"))) {
            bizMember.setMobile(StringUtil.getCheckString(params.get("mobile")));
        }
        if (!StringUtil.isNull(params.get("avatar"))) {
            bizMember.setAvatar(StringUtil.getCheckString(params.get("avatar")));
        }
        if (!StringUtil.isNull(params.get("selfpig"))) {
            bizMember.setSelfpig(StringUtil.getCheckString(params.get("selfpig")));
        }
        if (!StringUtil.isNull(params.get("pigjs1"))) {
            bizMember.setPigjs1(StringUtil.getCheckString(params.get("pigjs1")));
        }
        if (!StringUtil.isNull(params.get("pigjs2"))) {
            bizMember.setPigjs2(StringUtil.getCheckString(params.get("pigjs2")));
        }
        if (!StringUtil.isNull(params.get("pigxs1"))) {
            bizMember.setPigxs1(StringUtil.getCheckString(params.get("pigxs1")));
        }
        if (!StringUtil.isNull(params.get("pigxs2"))) {
            bizMember.setPigxs2(StringUtil.getCheckString(params.get("pigxs2")));
        }
        if (!StringUtil.isNull(params.get("pigsf1"))) {
            bizMember.setPigsf1(StringUtil.getCheckString(params.get("pigsf1")));
        }
        if (!StringUtil.isNull(params.get("pigsf2"))) {
            bizMember.setPigsf2(StringUtil.getCheckString(params.get("pigsf2")));
        }
        if (!StringUtil.isNull(params.get("endDate"))) {
            bizMember.setEndDate(StringUtil.getCheckString(params.get("endDate")));
        }
        if (!StringUtil.isNull(params.get("status"))) {
            bizMember.setStatus(StringUtil.getCheckString(params.get("status")));
        }
        memberDao.save(bizMember);
        return CommonResult.ok(bizMember);
    }
}
