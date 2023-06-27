package com.test.oa.untils;

import com.test.model.system.SysMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @return
 */
public class MenuHelper {
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
