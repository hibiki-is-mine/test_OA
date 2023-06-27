package com.test.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.common.result.Result;
import com.test.model.system.SysRole;
import com.test.oa.service.SysRoleService;
import com.test.vo.system.AssginRoleVo;
import com.test.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @return
 */
@Api("角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    SysRoleService sysRoleService;
    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result<List<SysRole>> getAll(){
        List<SysRole> list = sysRoleService.list();
        return Result.success(list);
    }
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result getWithPage(@PathVariable Long limit,
                              @PathVariable Long page,
                              SysRoleQueryVo sysRoleQueryVo){
        //创建page对象，传递分页相关参数
        //封装条件，判断条件是否为空，不为空则进行封装
        Page<SysRole> pageParam = new Page<>(page,limit);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName= sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)){
            //封装
            wrapper.like(SysRole::getRoleName,roleName);

        }
        Page<SysRole> paged = sysRoleService.page(pageParam, wrapper);
        return  Result.success(paged);
    }

    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result save(@RequestBody  SysRole sysRole){
        boolean is_success = sysRoleService.save(sysRole);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }
    @ApiOperation("根据ID查询")
    @GetMapping("get/{id}")

    public Result get(@PathVariable Long id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.success(sysRole);
    }
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole sysRole){

        boolean is_success = sysRoleService.updateById(sysRole);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }

    @ApiOperation("根据ID删除角色")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id){
        boolean is_success = sysRoleService.removeById(id);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }

    @ApiOperation("根据ID批量删除角色")
    @DeleteMapping("batchDelete")
    public Result batchDelete(@RequestBody List<Long> list){
        boolean is_success = sysRoleService.removeByIds(list);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        Map<String,Object> map = sysRoleService.getRoleByUserId(userId);
        return Result.success(map);
    }
    @ApiOperation("根据用户分配角色")
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);
        return Result.success();
    }
}

