package com.test.oa.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.process.ProcessType;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProcessTypeService extends IService<ProcessType> {
    List<ProcessType> findProcessType();
}
