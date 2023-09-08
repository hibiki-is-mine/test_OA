package com.test.oa.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.common.result.Result;
import com.test.model.process.Process;
import com.test.oa.process.service.ProcessService;
import com.test.vo.process.ProcessQueryVo;
import com.test.vo.process.ProcessVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @return
 */
@RestController
@RequestMapping("/process/oa-process")
public class ProcessController {
    @Autowired
    private ProcessService processService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        ProcessQueryVo processQueryVo){
        Page<ProcessVo> pageParam = new Page<>(page,limit);
        IPage<ProcessVo> processIPage = processService.selectPage(pageParam,processQueryVo);
        return Result.success(processIPage);
    }
}
