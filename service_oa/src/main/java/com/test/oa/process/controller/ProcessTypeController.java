package com.test.oa.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.common.result.Result;
import com.test.model.process.ProcessType;
import com.test.oa.process.service.ProcessTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/process/processType")
public class ProcessTypeController {

    @Autowired
    ProcessTypeService processTypeService;

    /**
     * 分页查询
     * @param page
     * @param limit
     * @return {@link Result}
     */
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "分页查询")
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,
                        @PathVariable Long limit){

        Page<ProcessType> pageParam = new Page<>(page,limit);
        Page<ProcessType> paged =processTypeService.page(pageParam);
        return Result.success(paged);
    }

    /**
     * 根据ID查询
     * @param id
     * @return {@link Result}
     */
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "根据ID查询")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        ProcessType type = processTypeService.getById(id);
        return Result.success(type);
    }

    /**
     * 新增
     * @param processType
     * @return {@link Result}
     */
    @PreAuthorize("hasAuthority('bnt.processType.add')")
    @ApiOperation(value = "添加")
    @PostMapping("save")
    public Result add(@RequestBody ProcessType processType){
        processTypeService.save(processType);
        return Result.success();

    }

    /**
     * 修改
     * @param processType
     * @return {@link Result}
     */
    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result update(@RequestBody ProcessType processType){
        processTypeService.updateById(processType);
        return Result.success();
    }

    /**
     * 删除
     * @param id
     * @return {@link Result}
     */
    @PreAuthorize("hasAuthority('bnt.processType.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        processTypeService.removeById(id);
        return Result.success();
    }

}
