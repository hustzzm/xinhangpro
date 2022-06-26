package com.pig.basic.shiro.auth;

import com.pig.basic.biz.ShiroService;
import com.pig.basic.constant.SystemConstant;
import com.pig.basic.shiro.token.JWTToken;
import com.pig.basic.shiro.token.JWTUtil;
import com.pig.basic.util.RedisUtil;
import com.pig.modules.system.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 自定义Realm
 * @author dolyw.com
 * @date 2018/8/30 14:10
 */
@Service
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private ShiroService shiroService;
//
//    @Autowired
//    private PfUserService userService;


    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JWTToken;
    }

    @Override
    /**
     * 授权(验证权限时候调用
     *@param  [principals]
     *@return org.apache.shiro.authz.AuthorizationInfo
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1. 从 PrincipalCollection 中来获取登录用户的信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String userCode = JWTUtil.getClaim(principals.toString(), SystemConstant.ACCOUNT);
//        /**
//         * 获取按钮权限
//         */
//        User userInfo = shiroService.getUserInfo(userCode);
//        List<String> buttonAction = userInfo.getButtonAction();
//        for (String action : buttonAction) {
//                //2.1.1添加按钮权限
//                simpleAuthorizationInfo.addStringPermission(action);
//        }
        //Integer userId = user.getUserId();
        //2.添加角色和权限
//        for (Role role : user.getRoles()) {
//            //2.1添加角色
//            simpleAuthorizationInfo.addRole(role.getRoleName());
//            for (Permission permission : role.getPermissions()) {
//                //2.1.1添加权限
//                simpleAuthorizationInfo.addStringPermission(permission.getPermission());
//            }
//        }
        return simpleAuthorizationInfo;
    }

    @Override
    /**
     * 认证(登陆时候调用)
     *@param  [token]
     *@return org.apache.shiro.authc.AuthenticationInfo
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        String token = (String) authToken.getCredentials();
        // 解密获得account，用于和数据库进行对比
        String account = JWTUtil.getClaim(token, SystemConstant.ACCOUNT);
        // 帐号为空
        if (StringUtils.isEmpty(account)) {
            throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");
        }
        // 查询用户是否存在
        User userDto = shiroService.selectUserByCode(account);
        if (userDto == null) {
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JWTUtil.verify(token) && RedisUtil.exists(SystemConstant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = RedisUtil.getObject(SystemConstant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JWTUtil.getClaim(token, SystemConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }
}
