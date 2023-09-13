package com.test.oa;

import com.test.common.jwt.JwtHelper;
import com.test.model.system.SysUser;
import com.test.oa.auth.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TokenTest {
    @Autowired
    SysUserService sysUserService;
    @Test
    public void test(){
        SysUser user1 = sysUserService.getById(7);//获取张财务token
        SysUser user2= sysUserService.getById(2);//获取王经理token
        String token1 = JwtHelper.createToken(user1.getId(), user1.getUsername());
        String token2 = JwtHelper.createToken(user2.getId(), user2.getUsername());
        System.out.println(token1);
        System.out.println(token2);
    }

}
