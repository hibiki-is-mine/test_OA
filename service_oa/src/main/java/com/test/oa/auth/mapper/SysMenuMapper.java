package com.test.oa.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @return
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> getMenuByUserId(@Param("userId")Long userId);
}
