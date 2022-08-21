package com.pig.modules.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TokenUtils {

    //设置过期时间
    private static final long EXPIRE_DATE = 12 * 60 * 60 * 1000;

    //token秘钥
    private static final String TOKEN_SECRET = "15d180b368829741c05cb31476e68b5d";

    public static String token(String openid, String mobile) {
        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String, Object> header = new HashMap<>();
            header.put("type", "JWT");
            header.put("algs", "HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("openid", openid)
                    .withClaim("mobile", mobile).withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("tokenUtils verify is error!", e);
            return null;
        }
        return token;
    }

    /**
     * @desc 验证token，通过返回true
     * @params [token]需要校验的串
     **/
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("tokenUtils verify is error!", e);
            return false;
        }
    }
}
