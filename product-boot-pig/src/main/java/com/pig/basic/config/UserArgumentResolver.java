package com.pig.basic.config;
import com.pig.basic.constant.SystemConstant;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.shiro.token.JWTUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求用户信息封装解析器
 */
@Configuration
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 1. 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(UserVo.class);
    }

    /**
     * @param methodParameter       入参集合
     * @param modelAndViewContainer model 和 view
     * @param nativeWebRequest      web相关
     * @param webDataBinderFactory  入参解析
     * @return 包装对象
     * @throws Exception exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String token = request.getHeader(SystemConstant.USER_HEADER);
        try{
            // 获取当前Token的帐号信息
            String account = JWTUtil.getClaim(token, SystemConstant.ACCOUNT);
            String userName = JWTUtil.getClaim(token, "userName");
            String roleId = JWTUtil.getClaim(token, "roleId");
            UserVo vo = new UserVo();
            vo.setUserCode(account);
            vo.setUserName(userName);
            vo.setRoleId(roleId);
            return vo;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
