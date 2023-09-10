package com.test.oa.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.process.Process;
import com.test.vo.process.ApprovalVo;
import com.test.vo.process.ProcessFormVo;
import com.test.vo.process.ProcessQueryVo;
import com.test.vo.process.ProcessVo;
import java.util.Map;


public interface ProcessService extends IService<Process> {
    /**
     * 获取审查列表
     * @return {@link IPage}<{@link ProcessVo}>
     */
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    /**
     * 部署activiti流程
     */
    void deployByZip(String deployPath);

    /**
     * 启动流程
     */
    void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Long page,Long size);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Long page,Long limit);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
