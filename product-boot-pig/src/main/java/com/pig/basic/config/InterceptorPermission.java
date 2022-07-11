package com.pig.basic.config;


import com.pig.basic.exception.MessageException;
import com.pig.modules.system.entity.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class InterceptorPermission implements HandlerInterceptor {


    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果是SpringMVC请求
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            PublicInterface publicInterface = method.getAnnotation(PublicInterface.class);
            if (publicInterface != null) {
                return true;
            }
            // 过滤微信api接口的认证
            if (request.getRequestURI().startsWith("/wx")) {
                return true;
            }
            User user = (User) request.getSession().getAttribute("currentUser");
            if (user == null) {
                response.setStatus(999);
                throw new MessageException("请求超时，请进入登录页面重新登录系统.");
            }
            if (user.getIsAdmin()) {
                return true;
            }
            throw new MessageException("缺少访问权限，请进入登录页面重新登录系统.");
        }
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

}
