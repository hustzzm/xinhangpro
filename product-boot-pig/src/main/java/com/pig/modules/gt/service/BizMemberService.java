package com.pig.modules.gt.service;

import com.pig.modules.gt.entity.BizMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 用户信息
不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表服务接口
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
public interface BizMemberService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BizMember queryById(String id);

    /**
     * 分页查询
     *
     * @param bizMember 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<BizMember> queryByPage(BizMember bizMember, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param bizMember 实例对象
     * @return 实例对象
     */
    BizMember insert(BizMember bizMember);

    /**
     * 修改数据
     *
     * @param bizMember 实例对象
     * @return 实例对象
     */
    BizMember update(BizMember bizMember);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}
