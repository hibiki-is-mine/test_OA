package com.test.oa;

import com.test.model.system.SysRole;
import com.test.oa.mapper.SysRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @return
 */
@SpringBootTest
public class ServiceTest {
    @Autowired
    SysRoleMapper sysRoleMapper;

    @Test
    public void SysRoleMapperGetAll(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        for (SysRole r: sysRoles
             ) {
            System.out.println(r);
        }
    }
}
