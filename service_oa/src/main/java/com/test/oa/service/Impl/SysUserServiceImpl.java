package com.test.oa.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.system.SysUser;
import com.test.oa.mapper.SysUserMapper;
import com.test.oa.service.SysUserService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

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
}
