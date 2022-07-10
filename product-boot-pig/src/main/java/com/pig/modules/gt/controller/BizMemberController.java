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
    /**
     * 服务对象
     */
    @Resource
    private BizMemberService bizMemberService;

    /**
     * 分页查询
     *
     * @param bizMember 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<BizMember>> queryByPage(BizMember bizMember, PageRequest pageRequest) {
        return ResponseEntity.ok(this.bizMemberService.queryByPage(bizMember, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<BizMember> queryById(@PathVariable("id") String id) {
        return ResponseEntity.ok(this.bizMemberService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param bizMember 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<BizMember> add(BizMember bizMember) {
        return ResponseEntity.ok(this.bizMemberService.insert(bizMember));
    }

    /**
     * 编辑数据
     *
     * @param bizMember 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<BizMember> edit(BizMember bizMember) {
        return ResponseEntity.ok(this.bizMemberService.update(bizMember));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(String id) {
        return ResponseEntity.ok(this.bizMemberService.deleteById(id));
    }

}

