package com.test.oa.process.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.process.ProcessRecord;
import com.test.model.system.SysUser;
import com.test.oa.auth.service.SysUserService;
import com.test.oa.process.mapper.ProcessRecordMapper;
import com.test.oa.process.service.ProcessRecordService;
import com.test.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {

    @Autowired
    private SysUserService sysUserService;
    @Override
    public void record(Long processId, Integer status, String description) {
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser user = sysUserService.getById(userId);

        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUser(user.getName());
        processRecord.setOperateUserId(userId);

        baseMapper.insert(processRecord);


    }
}
