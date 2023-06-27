package com.test.oa.controller;

import com.test.common.result.Result;
import com.test.model.system.SysMenu;
import com.test.oa.service.SysMenuService;
import com.test.oa.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @return
 */
@Api("菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    SysMenuService sysMenuService;
    @ApiOperation("获取菜单")
    @GetMapping("findNodes")
    public Result findNodes(){
        List<SysMenu> list =sysMenuService.findNodes();
        return Result.success(list);
    }
    @ApiOperation("保存")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu){
        boolean is_success = sysMenuService.save(sysMenu);
        if (is_success){
            return Result.success();
        }else return Result.fail();
    }
    @ApiOperation("修改")
    @PutMapping("update")
    public Result update(@RequestBody SysMenu sysMenu){
        boolean is_success = sysMenuService.updateById(sysMenu);
        if (is_success){
            return Result.success();
        }else return Result.fail();
    }
    @ApiOperation("删除")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id){
        boolean is_success = sysMenuService.deleteById(id);
        if (is_success){
            return Result.success();
        }else return Result.fail().message("不能删除");

    }


}
