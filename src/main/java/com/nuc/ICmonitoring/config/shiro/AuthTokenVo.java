package com.nuc.ICmonitoring.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author DeepBlue
 * @Date 2020/11/12 20:10
 */
public class AuthTokenVo implements AuthenticationToken {
    private String token;

    public AuthTokenVo(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }//获取认证码principal

    @Override
    public Object getCredentials() {
        return token;
    }
}
