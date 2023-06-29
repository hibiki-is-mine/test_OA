package com.test.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.system.SysUser;

/**
 * @return
 */
public interface SysUserService extends IService<SysUser> {
    boolean updateStatus(Integer userId, Integer status);

    SysUser getUserByUserName(String username);
}
