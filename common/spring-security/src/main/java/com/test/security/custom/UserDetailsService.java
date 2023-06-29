package com.test.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @return
 */
public interface UserDetailsService {
    //根据用户名获取用户对象，无法获取则抛出异常
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
