package com.test.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.system.SysMenu;
import com.test.vo.system.AssginMenuVo;
import com.test.vo.system.AssginRoleVo;
import com.test.vo.system.RouterVo;

import java.util.List;

/**
 * @return
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findNodes();

    boolean deleteById(Long id);

    List<SysMenu> getMenusByRoleId(Long roleId);

    //为角色分配Menu
    void doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId);

    List<String> findUserPermsByUserId(Long userId);
}
