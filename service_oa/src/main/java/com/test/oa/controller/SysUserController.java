package com.test.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.errorprone.annotations.Var;
import com.test.common.result.Result;
import com.test.model.system.SysUser;
import com.test.model.system.SysUser;
import com.test.oa.service.SysUserRoleService;
import com.test.oa.service.SysUserService;
import com.test.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @return
 */
@Api("用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserRoleService sysUserRoleService;
    
    @ApiOperation("用户分页查询")
    @GetMapping("{page}/{limit}")
    public Result getUserWithPage(@PathVariable Long limit,
                                  @PathVariable Long page,
                                  SysUserQueryVo sysUserQueryVo){
        Page<SysUser> pageParam =  new Page<>(page,limit);
        //封装条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String keyword = sysUserQueryVo.getKeyword();
        String timeBegin = sysUserQueryVo.getCreateTimeBegin();
        String timeEnd = sysUserQueryVo.getCreateTimeEnd();

        //判断当前哪个条件不为空，就根据哪个条件查询
        if (!StringUtils.isEmpty(keyword)){
            wrapper.like(SysUser::getUsername,keyword);
        }
         
        if (!StringUtils.isEmpty(timeBegin)) {
            //ge大于等于
            wrapper.ge(SysUser::getCreateTime,timeBegin);
        }
        if (!StringUtils.isEmpty(timeEnd)) {
            //le小于等于
            wrapper.le(SysUser::getCreateTime,timeEnd);
        }
        //调用mp方法实现条件分页查询
        IPage<SysUser> pageModel = sysUserService.page(pageParam,wrapper);
        
        return Result.success(pageModel);
    }

    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysUser sysUser){
        boolean is_success = sysUserService.save(sysUser);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }
    @ApiOperation("根据ID查询")
    @GetMapping("get/{id}")

    public Result get(@PathVariable Long id){
        SysUser sysUser = sysUserService.getById(id);
        return Result.success(sysUser);
    }
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysUser sysUser){

        boolean is_success = sysUserService.updateById(sysUser);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }

    @ApiOperation("根据ID删除角色")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id){
        boolean is_success = sysUserService.removeById(id);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }

    @ApiOperation("根据ID批量删除角色")
    @DeleteMapping("batchDelete")
    public Result batchDelete(@RequestBody List<Long> list){
        boolean is_success = sysUserService.removeByIds(list);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }
    @ApiOperation("修改用户是否可用")
    @PutMapping("updateStatus/{userId}/{status}")
    public Result updateStatus(@PathVariable Integer userId,
                               @PathVariable Integer status){

        boolean is_success = sysUserService.updateStatus(userId, status);
        if (is_success){
            return Result.success();
        }
        else return Result.fail();
    }

}
