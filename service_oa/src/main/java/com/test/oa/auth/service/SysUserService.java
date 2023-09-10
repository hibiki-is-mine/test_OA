package com.test.oa.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.system.SysUser;

import java.util.Map;

/**
 * @return
 */
public interface SysUserService extends IService<SysUser> {
    boolean updateStatus(Integer userId, Integer status);

    SysUser getUserByUserName(String username);

    Map<String, Object> getCurrentUser();
}
