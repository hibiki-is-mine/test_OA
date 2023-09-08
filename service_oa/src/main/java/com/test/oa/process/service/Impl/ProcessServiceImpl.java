package com.test.oa.process.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.process.Process;
import com.test.oa.process.mapper.ProcessMapper;
import com.test.oa.process.service.ProcessService;
import com.test.vo.process.ProcessQueryVo;
import com.test.vo.process.ProcessVo;
import org.springframework.stereotype.Service;

/**
 * @return
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {

        return baseMapper.selectPage(pageParam,processQueryVo);
    }
}
