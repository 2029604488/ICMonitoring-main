package com.nuc.ICmonitoring.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Map;

/**
 * @Author DeepBlue
 * @Date 2020/11/12 15:20
 * jwt的一些工具类方法
 */
@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secretKey}")
    private String SIGN;
    //生成token密匙，加密过程
    public String getToken(Map<String,String> map){
        // 使用默认时区和语言环境获得一个日历
        Calendar calendar=Calendar.getInstance();
        //设置Token60分钟过期
        calendar.add(Calendar.MINUTE,60);
        //创建一个Token
        JWTCreator.Builder builder=JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);//添加payload
        });
        builder.withExpiresAt(calendar.getTime());//设置过期时间
        return builder.sign(Algorithm.HMAC256(SIGN));//设置签名 密钥
    }
    //生成身份信息编码
    public String getIdentity(HttpServletRequest request){
        String token=request.getHeader("authorization");//获得的是http协议定义的文件头信息
        DecodedJWT decode=JWT.decode(token);
        Claim identity =decode.getClaim("identity");
        return identity.asString();
    }
    //验证对象,这里使用的加密算法和密钥必须与生成TOKEN时的相同否则无法验证
    public DecodedJWT verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SIGN);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return  verifier.verify(token);
    }
    public String getRequestToken(HttpServletRequest httpServletRequest){
        String token=httpServletRequest.getHeader("authorization");
        log.error("获取到jwtToken={}",token);
        return token;
    }
    public String getSIGN(){return SIGN;}
}
