package com.test.oa.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.process.Process;
import com.test.vo.process.ProcessQueryVo;
import com.test.vo.process.ProcessVo;

/**
 * @return
 */
public interface ProcessService extends IService<Process> {
    /**
     * 获取审查列表
     * @param pageParam
     * @param processQueryVo
     * @return {@link IPage}<{@link ProcessVo}>
     */
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    /**
     * 部署activiti流程
     * @param deployPath
     */
    void deployByZip(String deployPath);
}
