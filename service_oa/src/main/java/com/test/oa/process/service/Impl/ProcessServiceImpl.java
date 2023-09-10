package com.test.oa.process.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.model.process.Process;
import com.test.model.process.ProcessRecord;
import com.test.model.process.ProcessTemplate;
import com.test.model.system.SysUser;
import com.test.oa.auth.service.SysUserService;
import com.test.oa.process.mapper.ProcessMapper;
import com.test.oa.process.service.ProcessRecordService;
import com.test.oa.process.service.ProcessService;
import com.test.oa.process.service.ProcessTemplateService;
import com.test.security.custom.LoginUserInfoHelper;
import com.test.vo.process.ApprovalVo;
import com.test.vo.process.ProcessFormVo;
import com.test.vo.process.ProcessQueryVo;
import com.test.vo.process.ProcessVo;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessRecordService processRecordService;
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        return baseMapper.selectPage(pageParam,processQueryVo);
    }

    @Override
    public void deployByZip(String deployPath) {
        //通过InputStream获取zip压缩文件
        InputStream inputStream = this.
                getClass().
                getClassLoader().
                getResourceAsStream(deployPath);
        if (inputStream != null) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            repositoryService.
                    createDeployment().
                    addZipInputStream(zipInputStream).
                    deploy();
        }

    }

    @Override
    public void startUp(ProcessFormVo processFormVo) {
        //根据当前用户id获取用户信息
        //从LoginUserInfoHelper中获取用户id
        SysUser user = sysUserService.getById(LoginUserInfoHelper.getUserId());
        //根据审批模板id，查询模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());
        //保存提交的信息至oa_process
        Process process= new Process();
        //使用BeanUtils将processFormVo中的属性值复制到process
        BeanUtils.copyProperties(processFormVo,process);
        process.setStatus(1);//设置状态码
        process.setProcessCode(String.valueOf(System.currentTimeMillis()));//设置编号
        process.setUserId(user.getId());
        process.setFormValues(processFormVo.getFormValues());//表单json信息
        process.setTitle(user.getName()+"发起"+processTemplate.getName()+"申请");//设置标题
        baseMapper.insert(process);
        //启动流程实例(RuntimeService),包含三个参数
        //流程定义key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        //业务key
        String processKey = String.valueOf(process.getId());
        //流程参数(表单json转map)
        JSONObject jsonObject = JSON.parseObject(processFormVo.getFormValues());
        JSONObject formData = jsonObject.getJSONObject("formData");
        //遍历formData
        Map<String, Object> map = new HashMap<>(formData);
        //启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, processKey, map);
        //查询下一个审批人

        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        List<String> nameList = new ArrayList<>();
        for (Task t:taskList){
            String assigneeName = t.getAssignee();
            SysUser userByUserName = sysUserService.getUserByUserName(assigneeName);
            nameList.add(userByUserName.getName());
            //TODO 推送审批消息
        }

        //设置实例id
        process.setProcessInstanceId(processInstance.getId());
        //设置描述信息
        process.setDescription("等待"+ StringUtils.join(nameList.toArray()) +"审批");

        //关联业务和流程，更新process数据并记录
        baseMapper.updateById(process);
        processRecordService.record(process.getId(),1,"发起申请");

    }

    @Override
    public IPage<ProcessVo> findPending(Long page,Long size) {
        //封装查询条件，根据当前等于用户名称查询
        TaskQuery query = taskService
                .createTaskQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();

        long totalCount = query.count();
        int start = Math.toIntExact(page);
        int s = Math.toIntExact(size);
        List<Task> list = query.listPage(start, s);
        List<ProcessVo> processList = new ArrayList<>();
        for (Task item : list) {
            String processInstanceId = item.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (processInstance == null) {
                continue;
            }
            // 业务key
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Process process = this.getById(Long.parseLong(businessKey));
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(item.getId());
            processList.add(processVo);
        }
        IPage<ProcessVo> processVoIPage = new Page<ProcessVo>(page, size, totalCount);
        processVoIPage.setRecords(processList);
        return processVoIPage;

    }

    @Override
    public Map<String, Object> show(Long id) {
        //根据id查询process表获取流程信息
        Process process = baseMapper.selectById(id);

        //根据id查询process_record表获取流程记录信息
        LambdaQueryWrapper<ProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId,id);
        List<ProcessRecord> processRecordList = processRecordService.list(wrapper);

        //根据id查询模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getId());

        //判断当前用户是否可以审批,不能重复审批
        boolean isApprove =false;
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        for (Task task : taskList){
            String username = LoginUserInfoHelper.getUsername();
            if (task.getAssignee().equals(username)){
                isApprove=true;
            }
        }

        //查询的数据封装到Map集合中
        Map<String , Object> map = new HashMap<>();
        map.put("process",process);
        map.put("processRecordList" , processRecordList);
        map.put("processTemplate",processTemplate);
        map.put("isApprove",isApprove);
        return map;
    }

    @Override
    public void approve(ApprovalVo approvalVo) {
        //从ApprovalVo获取taskId
        String taskId = approvalVo.getTaskId();
        Map<String, Object> tasks = taskService.getVariables(taskId);

        //判断审批状态值status
        if (approvalVo.getStatus()==1){
            //status等于1则审批通过
            Map<String,Object> map = new HashMap<>();
            taskService.complete(taskId,map);
        }else {
            //status等于-1则审批驳回
            this.endTask(taskId);
        }

        String description = approvalVo.getStatus()==1 ? "审批通过" : "审批驳回";
        //将审批记录写入process_record表
        processRecordService.record(approvalVo.getProcessId(),
                                    approvalVo.getStatus(),
                                    description);

        //查询下一个审批人，如果查不到则更新process表
        //根据processId查询process实例
        Process process = baseMapper.selectById(approvalVo.getProcessId());
        //查询流程
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (!CollectionUtils.isEmpty(taskList)){
            //有下一个审批人
            List<String> nameList = new ArrayList<>();
            for (Task task: taskList){
                String assignee = task.getAssignee();
                SysUser user = sysUserService.getUserByUserName(assignee);
                nameList.add(user.getName());
                //TODO消息推送
            }
            //更新process流程信息
            process.setDescription("等待"+StringUtils.join(nameList.toArray())+"审批");
            process.setStatus(1);
            //没有下一个审批人
        }else {
            if (approvalVo.getStatus()==1){
                process.setDescription("审批通过");
                process.setStatus(2);
            }else {
                process.setDescription("审批驳回");
                process.setStatus(-1);
            }
        }
        baseMapper.updateById(process);
    }

    private void endTask(String taskId) {
        //根据taskId获取任务对象Task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        //根据流程定义模型BpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //获取结束流向节点
        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if (CollectionUtils.isEmpty(endEventList)){
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);

        //获取当前流向节点
        FlowNode currentFlowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //保存原始活动方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.add(currentFlowNode.getOutgoingFlows());
        //清理当前流向节点
        currentFlowNode.getOutgoingFlows().clear();
        //创建新的流向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlow");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);

        //当前节点指向新的方向
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //完成当前任务
        taskService.complete(taskId);

    }

    private List<Task> getCurrentTaskList(String id) {
        return taskService.createTaskQuery().processInstanceId(id).list();
    }

}
