package com.test.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.system.SysMenu;

import java.util.List;

/**
 * @return
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findNodes();

    boolean deleteById(Long id);
}
