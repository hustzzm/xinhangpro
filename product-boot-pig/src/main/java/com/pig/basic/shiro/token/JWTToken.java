package com.pig.basic.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JwtToken
 */
public class JWTToken implements AuthenticationToken {
    /**
     * Token
     */
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
