package com.pig.basic.shiro.token;

import cn.hutool.core.codec.Base64;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pig.basic.constant.SystemConstant;
import com.pig.basic.listener.PropertiesListenerConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
@Slf4j
public class JWTUtil {

    /**
     * 校验token是否正确
     */
    public static boolean verify(String token) {
        try {
            // 帐号加JWT私钥解密
            String secret = getClaim(token, SystemConstant.ACCOUNT) + Base64.encode(PropertiesListenerConfig.getProperty("encryptJWTKey"));
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("JWTToken认证解密出现UnsupportedEncodingException异常:" + e.getMessage());
            throw new RuntimeException("JWTToken认证解密出现UnsupportedEncodingException异常:" + e.getMessage());
        }
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            log.error("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
            throw new RuntimeException("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
        }
    }

    /**
     * 生成签名
     */
    public static String sign(String userCode,String userName,String roleId, String currentTimeMillis) {
        try {
            // 帐号加JWT私钥加密
            String secret = userCode + Base64.encode(PropertiesListenerConfig.getProperty("encryptJWTKey"));
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(System.currentTimeMillis() + Long.parseLong(PropertiesListenerConfig.getProperty("accessTokenExpireTime")) * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带account帐号信息
            return JWT.create()
                    .withClaim(SystemConstant.ACCOUNT, userCode)
                    .withClaim("userName",userName)
                    .withClaim("roleId",roleId)
                    .withClaim("currentTimeMillis", currentTimeMillis)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
            throw new RuntimeException("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
        }
    }

}
