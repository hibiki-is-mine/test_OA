package com.test.oa.process.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.process.ProcessTemplate;
import com.test.model.process.ProcessType;
import com.test.oa.process.mapper.ProcessTemplateMapper;
import com.test.oa.process.service.ProcessService;
import com.test.oa.process.service.ProcessTemplateService;
import com.test.oa.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate> implements ProcessTemplateService {

    @Autowired
    private ProcessTemplateMapper processTemplateMapper;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessService processService;

    @Override
    public IPage<ProcessTemplate> selectPage2(Page<ProcessTemplate> pageParam) {
        LambdaQueryWrapper<ProcessTemplate> queryWrapper = new LambdaQueryWrapper<ProcessTemplate>();
        queryWrapper.orderByDesc(ProcessTemplate::getId);
        IPage<ProcessTemplate> page = processTemplateMapper.selectPage(pageParam, queryWrapper);
        List<ProcessTemplate> processTemplateList = page.getRecords();

        List<Long> processTypeIdList = processTemplateList.stream().map(ProcessTemplate::getProcessTypeId).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(processTypeIdList)) {
            Map<Long, ProcessType> processTypeIdToProcessTypeMap = processTypeService.list(new LambdaQueryWrapper<ProcessType>().in(ProcessType::getId, processTypeIdList)).stream().collect(Collectors.toMap(ProcessType::getId, ProcessType -> ProcessType));
            for(ProcessTemplate processTemplate : processTemplateList) {
                ProcessType processType = processTypeIdToProcessTypeMap.get(processTemplate.getProcessTypeId());
                if(null == processType) continue;
                processTemplate.setProcessTypeName(processType.getName());
            }
        }
        return page;
    }

    @Override
    public IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam) {
        return baseMapper.selectPage(pageParam);
    }

    @Override
    public void publish(Long id) {
        //修改模板的状态值，1表示已发布
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);
        //调用processService中的deployByZip方法部署流程
        if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())){
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
        }

    }
}
