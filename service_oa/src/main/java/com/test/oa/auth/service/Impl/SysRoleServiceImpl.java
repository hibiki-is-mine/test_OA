package com.test.oa.auth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.system.SysRole;
import com.test.model.system.SysUserRole;
import com.test.oa.auth.mapper.SysRoleMapper;
import com.test.oa.auth.mapper.SysUserRoleMapper;
import com.test.oa.auth.service.SysRoleService;
import com.test.oa.auth.service.SysUserRoleService;
import com.test.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @return
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    //public Map<String ,Object> getRoleByUserId(Long userId) {
    @Override
    public Map<String, Object> getRoleByUserId(Long userId) {
        //查询所有的角色
        List<SysRole> allRolesList = this.list();

        //拥有的角色id
        List<SysUserRole> existUserRoleList = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId).select(SysUserRole::getRoleId));
        List<Long> existRoleIdList = existUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        //对角色进行分类
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole role : allRolesList) {
            //已分配
            if(existRoleIdList.contains(role.getId())) {
                assginRoleList.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assginRoleList);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //删除用户之前分配的数据
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId: roleIdList
             ) {
            if (StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole= new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRoleService.save(sysUserRole);
        }
    }
}
