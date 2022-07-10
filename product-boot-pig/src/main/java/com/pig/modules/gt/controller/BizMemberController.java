package com.pig.modules.gt.controller;

import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.service.BizMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户信息
不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表控制层
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
@RestController
@RequestMapping("bizMember")
public class BizMemberController {
}

