package com.pig.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig.modules.system.entity.User;
import com.pig.modules.system.entity.vo.UserVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 
 * @since 2020-04-24
 */
public interface UserService extends IService<User> {

    User getUserByUserName(String userCode);

    /**
     * 添加/修改用户
     * @param user
     * @return
     */
    boolean submit(User user);

    /**
     * 根据ID查询一条用户记录
     * @param id
     * @return
     */
    UserVO selectUserVoById(String id);


    /**
     * 给用户设置角色
     *
     * @param userIds
     * @param roleIds
     * @return
     */
    boolean grantRole(String userIds, String roleIds);

    /**
     * 重置密码
     * @param userIds
     * @return
     */
    boolean resetPassword(String userIds);

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param newPassword1
     * @return
     */
    boolean updatePassword(String userId, String oldPassword, String newPassword, String newPassword1);

}
