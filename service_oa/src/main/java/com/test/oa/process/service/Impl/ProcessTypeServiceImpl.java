package com.test.oa.process.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.process.ProcessType;
import com.test.oa.process.mapper.ProcessTypeMapper;
import com.test.oa.process.service.ProcessTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {
    @Override
    public List<ProcessType> findProcessType() {
        return baseMapper.findProcessType();
    }
}
