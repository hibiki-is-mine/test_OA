package com.test.oa;

import com.test.model.system.SysMenu;
import com.test.model.system.SysRole;
import com.test.oa.mapper.SysMenuMapper;
import com.test.oa.mapper.SysRoleMapper;
import com.test.oa.service.SysRoleService;
import com.test.oa.untils.MenuHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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
    @Autowired
    SysMenuMapper sysMenuMapper;

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
        List<Integer> list= new ArrayList<>();
        list.add(12);
    }
    @Test
    public void treeTest(){
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(null);
        List<SysMenu> sysMenuTree = MenuHelper.buildTree(sysMenuList);
        for (SysMenu s: sysMenuTree){
            System.out.println(s);
        }
    }
}
