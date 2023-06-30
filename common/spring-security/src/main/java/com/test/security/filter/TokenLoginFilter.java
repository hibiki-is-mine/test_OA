package com.test.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.common.jwt.JwtHelper;
import com.test.common.result.Result;
import com.test.common.utils.ResponseUtil;
import com.test.security.custom.CustomUser;
import com.test.vo.system.LoginVo;
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

/**
 * @return
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    //构造方法
    public TokenLoginFilter(AuthenticationManager authenticationManager){
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","post"));
    }

    /**
     * 尝试验证
     *
     * @param request  请求
     * @param response 响应
     * @return {@link Authentication}
     * @throws AuthenticationException 身份验证异常
     *///重写方法，获取输入的用户名和密码，调用方法进行认证
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        try {
            //获取输入用户信息
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(),LoginVo.class);
            //封装方法
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            //调用方法
            return this.getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //认证成功调用的方法
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        //获取当前认证用户对象
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        //生成token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(),
                customUser.getSysUser().getUsername());

        //返回
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        ResponseUtil.out(response, Result.success(map));
    }
    //认证失败调用的方法
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        ResponseUtil.out(response, Result.fail());
    }
}
