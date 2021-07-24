package com.nuc.ICmonitoring.config.shiro;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.nuc.ICmonitoring.service.interfaces.UserService;
import com.nuc.ICmonitoring.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author DeepBlue
 * @Date 2020/11/12 17:19
 */
@Slf4j
@Component
public class CustomRealm extends AuthorizingRealm{
    @Autowired
    private JwtUtils jwtUtils;
    @Resource
    private UserService userService;
    //该抽象方法 获取数据 获取授权的数据 该抽象方法返回类型AuthorizationInfo接口：
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        //获取身份信息 Principal用户名、手机号、邮箱地址等 一个主体可以有多个身份，但是必须有一个主身份(Primary Principal)
        log.error("进行授权-------------");
        return null;

    }





    @Override//为了让realm支持jwt的凭证校验
    public  boolean supports(AuthenticationToken token){return token instanceof  AuthTokenVo;}

    @Override
    //认证方法    获取配置文件中的用户信息 该抽象方法返回类型AuthenticationInfo接口：
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)throws AuthenticationException{
        log.info("进入认证逻辑");
        String token=(String) authenticationToken.getCredentials();
        //获得token中的用户信息
        DecodedJWT verify=jwtUtils.verify(token);
        log.error(verify.getPayload());
        //认证器 第一个参数为用户名或用户对象 第二个参数为密码 第三个参数为当前real名称
        return new SimpleAuthenticationInfo(token,token,getName());
    }
}

