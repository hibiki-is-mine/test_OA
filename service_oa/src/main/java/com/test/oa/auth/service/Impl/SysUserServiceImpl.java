package com.test.oa.auth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.system.SysUser;
import com.test.oa.auth.mapper.SysUserMapper;
import com.test.oa.auth.service.SysUserService;
import com.test.security.custom.LoginUserInfoHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @return
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public boolean updateStatus(Integer userId, Integer status) {
        SysUser sysUser = baseMapper.selectById(userId);
        sysUser.setStatus(status);
        int i = baseMapper.updateById(sysUser);
        return i == 1;
    }

    @Override
    public SysUser getUserByUserName(String username) {

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> getCurrentUser() {
        SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());
        Map<String ,Object> map = new HashMap<>();

        map.put("name",sysUser.getName());
        map.put("phone",sysUser.getPhone());
        //map.put("deptName",sysUser.getDeptName());
        //map.put("postName",sysUser.getPostName());

        return map;
    }
}
