package com.pig.modules.gt.service.impl;

import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.dao.BizMemberDao;
import com.pig.modules.gt.service.BizMemberService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 用户信息
不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表服务实现类
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
@Service("bizMemberService")
public class BizMemberServiceImpl implements BizMemberService {
    @Resource
    private BizMemberDao bizMemberDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BizMember queryById(String id) {
        return this.bizMemberDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param bizMember 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<BizMember> queryByPage(BizMember bizMember, PageRequest pageRequest) {
        long total = this.bizMemberDao.count(bizMember);
        return new PageImpl<>(this.bizMemberDao.queryAllByLimit(bizMember, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param bizMember 实例对象
     * @return 实例对象
     */
    @Override
    public BizMember insert(BizMember bizMember) {
        this.bizMemberDao.insert(bizMember);
        return bizMember;
    }

    /**
     * 修改数据
     *
     * @param bizMember 实例对象
     * @return 实例对象
     */
    @Override
    public BizMember update(BizMember bizMember) {
        this.bizMemberDao.update(bizMember);
        return this.queryById(bizMember.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.bizMemberDao.deleteById(id) > 0;
    }
}
