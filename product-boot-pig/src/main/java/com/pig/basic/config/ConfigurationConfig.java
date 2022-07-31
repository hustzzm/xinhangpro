package com.pig.basic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationConfig {
    @Value("${api.certPath:}")
    public String certPath;

    @Value("${api.appId:}")
    public String appId;

    @Value("${api.Appsecret:}")
    public String Appsecret;

    @Value("${api.mchID:}")
    public String mchID;

    @Value("${api.v2Key:}")
    public String v2Key;

    @Value("${api.notifyUrl:}")
    public String notifyUrl;
}
