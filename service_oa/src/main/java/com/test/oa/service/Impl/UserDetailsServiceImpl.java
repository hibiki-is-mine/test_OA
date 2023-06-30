package com.test.oa.service.Impl;

import com.test.model.system.SysUser;
import com.test.oa.service.SysUserService;
import com.test.security.custom.CustomUser;
import com.test.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据userName查询
        SysUser sysUser= sysUserService.getUserByUserName(username);
        if (sysUser==null){
            throw  new UsernameNotFoundException("用户不存在");
        }
        if (sysUser.getStatus()==0){
            throw  new UsernameNotFoundException("账号停用");
        }
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
