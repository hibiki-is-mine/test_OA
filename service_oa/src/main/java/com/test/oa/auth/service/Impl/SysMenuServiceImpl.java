package com.test.oa.auth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.system.SysMenu;
import com.test.model.system.SysRoleMenu;
import com.test.oa.auth.mapper.SysMenuMapper;
import com.test.oa.auth.mapper.SysRoleMenuMapper;
import com.test.oa.auth.service.SysMenuService;
import com.test.oa.auth.service.SysRoleMenuService;
import com.test.oa.auth.utils.MenuHelper;
import com.test.vo.system.AssginMenuVo;
import com.test.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @return
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Override
    public List<SysMenu> findNodes() {
        //查询所有数据
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        return MenuHelper.buildTree(sysMenuList);
    }

    @Override
    public boolean deleteById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId,id);
        Integer count = baseMapper.selectCount(wrapper);
        if (count==0){
            baseMapper.deleteById(id);
        }
        return count <= 0;
    }

    @Override
    public List<SysMenu> getMenusByRoleId(Long roleId) {

        //查询所有菜单，添加条件状态值status=1
        LambdaQueryWrapper<SysMenu> wrapperSysMenu = new LambdaQueryWrapper<>();
        wrapperSysMenu.eq(SysMenu::getStatus,1);
        List<SysMenu> allSysMenuList = baseMapper.selectList(wrapperSysMenu);

        //根据角色id roleId查询 角色菜单关系表里面 角色id对应所有的菜单id
        LambdaQueryWrapper<SysRoleMenu> wrapperSysRoleMenu = new LambdaQueryWrapper<>();
        wrapperSysRoleMenu.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(wrapperSysRoleMenu);

        //使用流根据获取菜单id，获取对应菜单对象
        List<Long> menuIdList = sysRoleMenuList.
                stream().
                map(SysRoleMenu::getMenuId).
                collect(Collectors.toList());

        //拿着菜单id 和所有菜单集合里面id进行比较，如果相同封装
        allSysMenuList.stream().forEach(item -> {
            if(menuIdList.contains(item.getId())) {
                item.setSelect(true);
            } else {
                item.setSelect(false);
            }
        });

        //返回规定树形显示格式菜单列表
        List<SysMenu> sysMenuList = MenuHelper.buildTree(allSysMenuList);
        return sysMenuList;
    }
    //为角色分配Menu
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //根据roleId删除原先的RoleMenu
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId,assginMenuVo.getRoleId());
        sysRoleMenuMapper.delete(wrapper);
        //获取新的menuIdList并写入数据库
        for (Long menuId: assginMenuVo.getMenuIdList()){
            if (!StringUtils.isEmpty(menuId)){
                SysRoleMenu sysRoleMenu= new SysRoleMenu();
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenu.setRoleId(assginMenuVo.getRoleId());

                sysRoleMenuMapper.insert(sysRoleMenu);
            }

        }

    }

    /**
     * 通过用户id找到用户菜单列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link RouterVo}>
     */
    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList;
        //如果userId==1，即管理员则显示所有菜单
        if (userId==1L){
            //查询所有可用菜单
            LambdaQueryWrapper<SysMenu> wrapper= new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);

        }
        else {
            sysMenuList = baseMapper.getMenuByUserId(userId);
        }
        List<SysMenu> menuTreeList = MenuHelper.buildTree(sysMenuList);
        List<RouterVo> routerList = MenuHelper.buildRouter(menuTreeList);

        return routerList;
    }

    /**
     * 通过用户id找到允许操作的按键列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> findUserPermsByUserId(Long userId) {

        List<SysMenu> sysMenuList;
        //判断是否是userId是否等于1，等于1则是管理员，开放所有按键
        if (userId==1){
            LambdaQueryWrapper<SysMenu> wrapper= new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            sysMenuList = baseMapper.selectList(wrapper);
        }
        //如果不是管理员，根据id查询
        else {
            sysMenuList = baseMapper.getMenuByUserId(userId);
        }
        //通过stream流获取sysMenuList中的perms并放入list集合中
        List<String> permsList = sysMenuList.stream()
                .filter(item -> item.getType()==2)
                .map(SysMenu::getPerms)
                .collect(Collectors.toList());
        return permsList;
    }

/*    @Override
    public boolean updateMenu(SysMenu sysMenu) {
        //获取判断当前修改的菜单是否具有子菜单
        List<SysMenu> sysMenuList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, sysMenu.getId()));
        if(CollectionUtils.isEmpty(sysMenuList)){//如果是空说明没有子节点

        }

    }*/

}
