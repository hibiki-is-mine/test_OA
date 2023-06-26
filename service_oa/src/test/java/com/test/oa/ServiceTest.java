package com.test.oa;

import com.test.model.system.SysRole;
import com.test.oa.mapper.SysRoleMapper;
import com.test.oa.service.SysRoleService;
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
    @Autowired
    SysRoleService sysRoleService;

    @Test
    public void SysRoleMapperGetAll(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        for (SysRole r: sysRoles
             ) {
            System.out.println(r);
        }
    }
    @Test
    public void saveTest(){
        SysRole sysRole =  new SysRole();
        sysRole.setRoleName("testsave");
        sysRole.setRoleCode("6");
        sysRole.setId(14L);
        boolean save = sysRoleService.updateById(sysRole);
        System.out.println(save);
    }
}
