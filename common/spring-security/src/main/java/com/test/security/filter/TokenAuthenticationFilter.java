package com.test.security.filter;

import com.test.common.jwt.JwtHelper;
import com.test.common.result.Result;
import com.test.common.result.ResultCodeEnum;
import com.test.common.utils.ResponseUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Security;
import java.util.Collections;


public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        //如果是登录接口，直接放行
        if(request.getRequestURI().equals("/admin/system/index/login")){
            chain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication!=null){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request,response);
        }
        else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    /**
     * 判断请求头是否包含token信息
     *
     * @param request 请求
     * @return {@link UsernamePasswordAuthenticationToken}
     */
    private UsernamePasswordAuthenticationToken getAuthentication (HttpServletRequest request) {
        String token = request.getHeader("header");
        if(!StringUtils.isEmpty(token)){
            String userName = JwtHelper.getUsername(token);
            if (StringUtils.isEmpty(userName)){
                return new UsernamePasswordAuthenticationToken(userName,null, Collections.emptyList());
            }
        }
        return null;
    }
}
