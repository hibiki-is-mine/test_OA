package com.test.oa.process.controller;

import com.test.common.result.Result;
import com.test.oa.process.service.ProcessService;
import com.test.oa.process.service.ProcessTemplateService;
import com.test.oa.process.service.ProcessTypeService;
import com.test.vo.process.ApprovalVo;
import com.test.vo.process.ProcessFormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "审批流程管理")
@RestController
@RequestMapping("/admin/process")
@CrossOrigin//允许跨域
public class ProcessControllerEmp {
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private ProcessService processService;

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
    @PostMapping("/starUp")
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
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id){
        Map<String ,Object> map = processService.show(id);
        return Result.success(map);
    }

    @ApiOperation("实现审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo){
        processService.approve(approvalVo);
        return Result.success();

    }

}
