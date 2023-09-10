package com.test.oa.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.wechat.Menu;
import com.test.vo.wechat.MenuVo;

import java.util.List;

/**
 * @return
 */
public interface MenuService extends IService<Menu> {
    List<MenuVo> findMenuInfo();
}
