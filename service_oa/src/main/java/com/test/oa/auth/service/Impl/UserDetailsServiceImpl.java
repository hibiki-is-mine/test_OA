package com.test.oa.auth.service.Impl;

import com.test.model.system.SysUser;
import com.test.oa.auth.service.SysMenuService;
import com.test.oa.auth.service.SysUserService;
import com.test.security.custom.CustomUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

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
        //根据userid查询用户操作权限数据
        List<String> perms = sysMenuService.findUserPermsByUserId(sysUser.getId());
        //用于封装权限数据
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();

        //遍历集合来封装
        for (String s: perms
             ) {
            simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(s.trim()));
        }

        return new CustomUser(sysUser, simpleGrantedAuthorityList);
    }
}
