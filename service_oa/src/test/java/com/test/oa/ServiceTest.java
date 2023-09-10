package com.test.oa;

import com.test.model.system.SysMenu;
import com.test.model.system.SysRole;
import com.test.oa.auth.mapper.SysMenuMapper;
import com.test.oa.auth.mapper.SysRoleMapper;
import com.test.oa.auth.service.SysMenuService;
import com.test.oa.auth.service.SysRoleService;
import com.test.oa.auth.utils.MenuHelper;
import com.test.oa.wechat.service.MenuService;
import com.test.vo.wechat.MenuVo;
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
    @Autowired
    SysMenuService sysMenuService;
    @Autowired
    MenuService menuService;

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
    @Test
    public void selectMenuByRoleId(){
        List<SysMenu> menuList = sysMenuService.getMenusByRoleId(10L);
        for (SysMenu m: menuList){
            System.out.println(m);
        }
    }
    @Test
    public void testMenuList(){
        List<MenuVo> menuInfo = menuService.findMenuInfo();
        System.out.println(menuInfo);
    }
}
