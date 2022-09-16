package com.pig.basic.config;


import com.pig.basic.exception.MessageException;
import com.pig.basic.util.StringUtil;
import com.pig.modules.core.TokenUtils;
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
            // 获取token的接口放开
//            if ("/wx/login/getToken".equals(request.getRequestURI())
//                || request.getRequestURI().indexOf("/wx/login/getPhone") >-1
//                || request.getRequestURI().indexOf("/wx/member/getbyaccount") >-1
//                || request.getRequestURI().indexOf("/wx/login/WxOpenData") >-1) {
//                return true;
//            }
            // 进行token认证
            if (request.getRequestURI().startsWith("/wx")) {
                if(
                        request.getRequestURI().indexOf("/wx/pay/unifiedOrder") >-1
                                || request.getRequestURI().indexOf("/wx/pay/reunifiedOrder") >-1
                                || request.getRequestURI().indexOf("/wx/pay/orderQuery") >-1
                                || request.getRequestURI().indexOf("/wx/pay/supperunifiedOrder") >-1
                                || request.getRequestURI().indexOf("/wx/pay/ugrorderQuery") >-1
                                || request.getRequestURI().indexOf("/wx/booking/submit") >-1
                                || request.getRequestURI().indexOf("/wx/pay/quickunifiedOrder") >-1
//                                || request.getRequestURI().indexOf("/wx/base/uploadImageAndSave") >-1
                                || request.getRequestURI().indexOf("/wx/member/insertOrUpdate") >-1
                ){
                    // 从header里获取请求头token
                    String token = request.getHeader("token");
                    if (StringUtil.isEmpty(token)) {
                        throw new MessageException("token为空，请退出小程序重新进入.");
                    }
                    if (TokenUtils.verify(token)) {
                        return true;
                    } else {
                        throw new MessageException("token失效，请退出小程序重新进入.");
                    }
                }else{
                    return true;
                }
            }
            // 过滤微信api接口的认证
            if (request.getRequestURI().startsWith("/static")) {
                return true;
            }
            User user = (User) request.getSession().getAttribute("currentUser");
            if (user == null) {
                response.setStatus(999);
                throw new MessageException("请求超时，请返回登录窗口.");
            }
            if (user.getIsAdmin()) {
                return true;
            }
            throw new MessageException("缺少访问权限，请进入登录页面重新登录系统.");
        }
        return false;
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
