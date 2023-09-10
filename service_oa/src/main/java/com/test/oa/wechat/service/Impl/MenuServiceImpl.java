package com.test.oa.wechat.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.wechat.Menu;
import com.test.oa.wechat.mapper.MenuMapper;
import com.test.oa.wechat.service.MenuService;
import com.test.vo.wechat.MenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @return
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public List<MenuVo> findMenuInfo() {
        //获取所有子菜单
        List<Menu> childrenMenuList = baseMapper.selectList(new LambdaQueryWrapper<Menu>().ne(Menu::getParentId,0));
        //查询所有父菜单
        List<Menu> parentMenuList = baseMapper.selectList( new LambdaQueryWrapper<Menu>().eq(Menu::getParentId,0));

        List<MenuVo> menuVoList = new ArrayList<>();
        //遍历父级菜单
        for (Menu menu:parentMenuList){
            //新建一个MenuVo对象，并将Menu中的属性值复制到MenuVo中
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu,menuVo);

            List<MenuVo> children = new ArrayList<>();
            for (Menu menu2: childrenMenuList){
                if (Objects.equals(menu2.getParentId(), menu.getId())){
                    MenuVo menuVo2 = new MenuVo();
                    BeanUtils.copyProperties(menu2,menuVo2);
                    children.add(menuVo2);
                }
            }
            menuVo.setChildren(children);
            menuVoList.add(menuVo);
        }
        return menuVoList;
    }
}
