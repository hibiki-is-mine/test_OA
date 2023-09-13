package com.test.oa.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.common.result.Result;
import com.test.model.process.Process;
import com.test.oa.auth.service.SysUserService;
import com.test.oa.process.service.ProcessService;
import com.test.oa.process.service.ProcessTemplateService;
import com.test.oa.process.service.ProcessTypeService;
import com.test.vo.process.ApprovalVo;
import com.test.vo.process.ProcessFormVo;
import com.test.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "审批流程管理")
@RestController
@RequestMapping("/admin/process")
//@CrossOrigin//允许跨域
public class ProcessControllerEmp {
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("获取全部审批类型以及该类型下的所有模板")
    @GetMapping("findProcessType")
    public Result findProcessType(){
        return Result.success(processTypeService.findProcessType());
    }

    @ApiOperation("根据模板id得到对应模板")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable Long processTemplateId){
        return Result.success(processTemplateService.getById(processTemplateId));
    }
    @ApiOperation("启动流程")
    @PostMapping("/startUp")
    public Result start(@RequestBody ProcessFormVo processFormVo){
        processService.startUp(processFormVo);
        return Result.success();
    }

    @ApiOperation("查询代办任务")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(
                              @PathVariable Long page,
                              @PathVariable Long limit){
        //Page<Process> pageParam = new Page<>(page,limit);
        return Result.success(processService.findPending(page,limit));
    }
    @ApiOperation("查看审批详情信息")
    @GetMapping("/show/{id}")
    public Result show(@PathVariable Long id){
        Map<String ,Object> map = processService.show(id);
        return Result.success(map);
    }

    @ApiOperation("实现审批")
    @PostMapping("/approve")
    public Result approve(@RequestBody ApprovalVo approvalVo){
        processService.approve(approvalVo);
        return Result.success();

    }
    @ApiOperation("获取已处理审批")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcess(@PathVariable Long page,
                               @PathVariable Long limit){

        return Result.success(processService.findProcessed(page,limit)) ;
    }
    @ApiOperation("获取已发起任务")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@PathVariable Long page,
                              @PathVariable Long limit){
        Page<ProcessVo> pageParam = new Page<>(page,limit);
        IPage<ProcessVo> pageModel =  processService.findStarted(pageParam);
        return Result.success(pageModel);
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("getCurrentUser")
    public Result getCurrentUser(){
        Map<String ,Object> map = sysUserService.getCurrentUser();
        return Result.success(map);
    }

}
