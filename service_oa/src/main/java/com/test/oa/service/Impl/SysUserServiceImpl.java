package com.test.oa.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.system.SysUser;
import com.test.oa.mapper.SysUserMapper;
import com.test.oa.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @return
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
