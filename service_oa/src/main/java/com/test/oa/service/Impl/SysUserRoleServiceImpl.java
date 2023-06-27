package com.test.oa.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.system.SysUserRole;
import com.test.oa.mapper.SysUserMapper;
import com.test.oa.mapper.SysUserRoleMapper;
import com.test.oa.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @return
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper,SysUserRole> implements SysUserRoleService {
}
