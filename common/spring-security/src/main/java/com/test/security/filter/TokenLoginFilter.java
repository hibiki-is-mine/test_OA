package com.test.security.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.common.jwt.JwtHelper;
import com.test.common.result.Result;
import com.test.common.utils.ResponseUtil;
import com.test.security.custom.CustomUser;
import com.test.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TokenLoginFilter  extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;
    //构造方法
    public TokenLoginFilter(AuthenticationManager authentication
            ,RedisTemplate redisTemplate){
        this.setAuthenticationManager(authentication);
        this.setPostOnly(false);
        //设置登录接口
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index","POST"));
        this.redisTemplate = redisTemplate;
    }

    /**
     * 尝试验证
     *
     * @param request  请求
     * @param response 响应
     * @return {@link Authentication}
     *///重写登录验证过程
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response){
        try {
            //通过流获取request中的LoginVo对象
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);

            //封装UsernamePasswordAuthenticationToken对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            //调用SpringSecurity认证方法
            return  this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //认证成功的方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth)
            throws IOException, ServletException {
        //获取当前用户
        CustomUser customUser = (CustomUser)auth.getPrincipal();
        //生成token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(),
                customUser.getSysUser().getUsername());

        //获取当前用户权限数据，放到Redis里面 key：username   value：权限数据
        redisTemplate.opsForValue().set(customUser.getUsername(),
                JSON.toJSONString(customUser.getAuthorities()));

        //返回
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        ResponseUtil.out(response, Result.success(map));
    }
    //认证失败的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        ResponseUtil.out(response,Result.fail());
    }

}
