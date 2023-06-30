package com.test.oa.utils;

import com.test.model.system.SysMenu;
import com.test.vo.system.MetaVo;
import com.test.vo.system.RouterVo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @return
 */
public class MenuHelper {
    /**
     * 构建树
     *
     * @param sysMenuList 系统菜单列表
     * @return {@link List}<{@link SysMenu}>
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList){
        Map<Long,SysMenu> map = new HashMap<>();
        for (SysMenu menu: sysMenuList
             ) {
            List<SysMenu> children = new ArrayList<>();
            menu.setChildren(children);
            map.put(menu.getId(),menu);
        }
        List<SysMenu> rootList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId() == 0) {
                rootList.add(sysMenu);
            } else {
                SysMenu parent = map.get(sysMenu.getParentId());
                List<SysMenu> children = parent.getChildren();
                children.add(sysMenu);
            }
        }
        return rootList;
    }


    public static List<RouterVo> buildRouter(List<SysMenu> menuTreeList) {
        //创建list集合
        List<RouterVo> routerList = new ArrayList<>();
        for (SysMenu menu: menuTreeList
             ) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(),menu.getIcon()));
            //封装下一层数据
            List<SysMenu> children = menu.getChildren();

            //判断菜单类型中是否会包含隐藏路由
            if (menu.getType()==1){
                List<SysMenu> menuList = children
                        .stream()
                        .filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu m: menuList
                     ) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getPath(m));
                    hiddenRouter.setComponent(m.getComponent());
                    hiddenRouter.setMeta(new MetaVo(m.getName(),m.getIcon()));
                    routerList.add(hiddenRouter);
                }
            }else {
                if (!CollectionUtils.isEmpty(children)){
                    router.setAlwaysShow(true);
                    router.setChildren(buildRouter(children));
                }

            }
            routerList.add(router);
        }
        return routerList;
    }

    /**
     * 构建路由器
     *
     * @param menus 菜单
     * @return {@link List}<{@link RouterVo}>
     */
    public static List<RouterVo> buildRouter2(List<SysMenu> menus) {
        //创建list集合
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
            //如果当前是菜单，需将按钮对应的路由加载出来，如：“角色授权”按钮对应的路由在“系统管理”下面
            if(menu.getType() == 1) {
                List<SysMenu> hiddenMenuList = children
                        .stream()
                        .filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    router.setAlwaysShow(true);
                    router.setChildren(buildRouter2(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    public static String getPath(SysMenu menu){
        String routerPath="/"+menu.getPath();
        if(menu.getParentId()!=0){
            routerPath= menu.getPath();
        }
        return routerPath;
    }
    /*public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId().longValue() == 0) {
                trees.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }

    *//**
     * 递归查找子节点
     * @param treeNodes
     * @return
     *//*
    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> treeNodes) {
        sysMenu.setChildren(new ArrayList<SysMenu>());

        for (SysMenu it : treeNodes) {
            if(sysMenu.getId().longValue() == it.getParentId().longValue()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return sysMenu;
    }*/
}
