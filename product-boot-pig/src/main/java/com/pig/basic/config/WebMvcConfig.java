package com.pig.basic.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Value("${report.root.path}")
//    private String rootPath;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UserArgumentResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/report/file/**").addResourceLocations("file:"+rootPath);
        registry.addResourceHandler("/monitor/file/**").addResourceLocations("file:/");
        registry.addResourceHandler("/deliver/file/**").addResourceLocations("file:/");
        registry.addResourceHandler("/dp/file/**").addResourceLocations("file:/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
        converters.add(gsonHttpMessageConverters());
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverters() {
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//        gsonConverter.setGson(gson);
        return gsonConverter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration permission = registry.addInterceptor(new InterceptorPermission());
        permission.addPathPatterns("/**");
    }


}
