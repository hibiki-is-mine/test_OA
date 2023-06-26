package com.test.oa.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.system.SysRole;
import com.test.oa.mapper.SysRoleMapper;
import com.test.oa.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * @return
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
