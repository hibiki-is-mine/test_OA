package com.test.security.filter;

import com.alibaba.fastjson.JSON;
import com.test.common.jwt.JwtHelper;
import com.test.common.result.Result;
import com.test.common.result.ResultCodeEnum;
import com.test.common.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate=redisTemplate;
    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
        }
    }

    /**
     * 判断请求头是否具有token
     *
     * @param request 请求
     * @return {@link UsernamePasswordAuthenticationToken}
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            //不为空则表示已经登录
            String username = JwtHelper.getUsername(token);
            logger.info("username:"+username);
            if (!StringUtils.isEmpty(username)) {
                //认成功后获取权限数据
                String auth = (String) redisTemplate.opsForValue().get(username);
                //将redis湖区字符串放入集合中
                if (!StringUtils.isEmpty(auth)){
                    List<Map> list = JSON.parseArray(auth, Map.class);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (Map m: list
                    ) {
                        authorities.add(new SimpleGrantedAuthority((String)m.get("authority")));
                    }
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
                else return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());


            }
        }
        return null;
    }
}
