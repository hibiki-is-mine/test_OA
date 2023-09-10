package com.test.oa.process.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.test.model.process.ProcessRecord;

public interface ProcessRecordService extends IService<ProcessRecord> {
    void record(Long processId,Integer status,String description);
}
