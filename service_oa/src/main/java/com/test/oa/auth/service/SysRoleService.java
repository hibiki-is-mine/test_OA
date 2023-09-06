package com.test.oa.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.system.SysRole;
import com.test.vo.system.AssginRoleVo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @return
 */
public interface SysRoleService extends IService<SysRole> {
    Map<String , Object> getRoleByUserId(Long userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
