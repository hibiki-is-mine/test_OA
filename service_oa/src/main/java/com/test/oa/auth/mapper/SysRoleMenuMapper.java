package com.test.oa.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.model.system.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @return
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    List<Long> selectByRoleId(@Param("roleId") Long roleId);
}
