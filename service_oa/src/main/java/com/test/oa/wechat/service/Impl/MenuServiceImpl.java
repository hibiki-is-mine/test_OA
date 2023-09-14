package com.test.oa.wechat.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.wechat.Menu;
import com.test.oa.wechat.mapper.MenuMapper;
import com.test.oa.wechat.service.MenuService;
import com.test.vo.wechat.MenuVo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @return
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private WxMpService wxMpService;

    /**
     * 获取菜单信息
     *
     * @return {@link List}<{@link MenuVo}>
     */
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

    /**
     * 推送菜单至微信
     *
     * @return
     */
    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://refrain.v1.idcfengye.com/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        //http://10.44.6.173:9090#
                        view.put("url", "http://refrain.v1.idcfengye.com#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMenuKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        //return button;
        try {
            wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMenu() {
        try{
            wxMpService.getMenuService().menuDelete();
        }catch (WxErrorException e){
            throw new RuntimeException(e);
        }
    }
}
