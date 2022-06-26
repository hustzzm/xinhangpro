package com.pig.basic.shiro;

import com.pig.basic.shiro.auth.AuthRealm;
import com.pig.basic.shiro.cache.CustomCacheManager;
import com.pig.basic.shiro.token.JWTFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 */
@Configuration
public class ShiroConfig {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(AuthRealm authRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 使用自定义Realm
        defaultWebSecurityManager.setRealm(authRealm);
        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        // 设置自定义Cache缓存
        defaultWebSecurityManager.setCacheManager(new CustomCacheManager());
        return defaultWebSecurityManager;
    }

    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shirFilter(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        // 自定义过滤（关键）
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JWTFilter());
        shiroFilter.setFilters(filters);
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/druid/**", "anon");
        filterMap.put("/auth/login", "anon");
        filterMap.put("/import/template", "anon");
        filterMap.put("/dpimport/**", "anon");
        filterMap.put("/wesimport/**", "anon");
//        filterMap.put("/import/download", "anon");
        filterMap.put("/chipinfo/downloadCaseInfo", "anon");
        filterMap.put("/report/downLoadReport", "anon");
        filterMap.put("/syncase/unCompleteSample", "anon");
        filterMap.put("/menu/authRoutes", "anon");
        filterMap.put("/dataResult/picZip", "anon");
        filterMap.put("/deliverData/report", "anon");
        filterMap.put("/dataAnalysis/download", "anon");
        filterMap.put("/dp/file/**", "anon");
        filterMap.put("/**", "anon");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        // 这里不需要设置什么登陆路径之类的
        return shiroFilter;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题，
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
