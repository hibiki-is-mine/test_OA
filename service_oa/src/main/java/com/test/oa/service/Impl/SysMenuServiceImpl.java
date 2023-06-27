package com.test.oa.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.system.SysMenu;
import com.test.oa.mapper.SysMenuMapper;
import com.test.oa.service.SysMenuService;
import com.test.oa.untils.MenuHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @return
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
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
        return count <= 0;
    }
}
