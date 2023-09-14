package com.test.oa.wechat.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.test.model.process.Process;
import com.test.model.process.ProcessTemplate;
import com.test.model.system.SysUser;
import com.test.oa.auth.service.SysUserService;
import com.test.oa.process.service.ProcessService;
import com.test.oa.process.service.ProcessTemplateService;
import com.test.oa.wechat.service.MessageService;
import com.test.security.custom.LoginUserInfoHelper;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.checkerframework.checker.units.qual.A;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @return
 */
@Service
public class MessageServiceImpl  implements MessageService {

    //微信消息接口模板ID
    private final String MODEL_ID_1 ="w5Mt8SogErifQGqX88H5ENyLExR-reczwfn_Qv9MSfk";
    private final String MODEL_ID_2 ="fxFMpTMS36xCiP3tQ92ctM-3HR2Ew4ANmyipxdBeiMg";

    @Autowired
    private ProcessService processService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private WxMpService wxMpService;

    @Override
    public void pushPendingMessage(Long processId, Long userId, String taskId) {
        //查询流程信息
        Process process = processService.getById(processId);
        //查询下一个审批人员信息
        SysUser user = sysUserService.getById(userId);
        //获取模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        //获取提交人信息
        SysUser userSubmit = sysUserService.getById(process.getUserId());

        //设置消息发送的信息
        WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage
                .builder()
                .toUser(user.getOpenId())//被推送用户的openId
                .templateId(MODEL_ID_1)//消息接口模板ID
                .url("http://refrain.v1.idcfengye.com/#/show/" + processId + "/" + taskId)//跳转地址
                .build();
        //获取具体信息,JSON数据中的值
        StringBuffer content=getContent(process);

        //设置具体内容.addData(模板内容关键字，消息，颜色)
        wxMpTemplateMessage
                .addData(new WxMpTemplateData("first"//提交人姓名
                        ,userSubmit.getName()
                        +"提交了"+processTemplate.getName()
                        +"审批申请，请注意查看。"
                        , "#272727"))
                .addData(new WxMpTemplateData("keyword1"//审批编号
                        ,process.getProcessCode()
                        ,"#272727"))
                .addData(new WxMpTemplateData("keyword2"//提交时间
                        , new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss")
                        , "#272727"))
                .addData(new WxMpTemplateData("content"//具体内容
                        ,content.toString()
                        ,"#272727"));
        try{
            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
        }catch (WxErrorException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void pushProcessedMessage(Long processId, Long userId, Integer status) {
        Process process = processService.getById(processId);
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        SysUser sysUser = sysUserService.getById(userId);
        SysUser currentSysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        String openid = sysUser.getOpenId();

        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openid)//要推送的用户openid
                .templateId(MODEL_ID_2)//模板id
                .url("http://refrain.v1.idcfengye.com/#/show/"+processId+"/0")//点击模板消息要访问的网址
                .build();
        StringBuffer content=getContent(process);
        templateMessage
                .addData(new WxMpTemplateData("first"
                        ,"你发起的"
                        +processTemplate.getName()
                        +"审批申请已经被处理了，请注意查看。"
                        ,"#272727"))
                .addData(new WxMpTemplateData("keyword1"//审批编号
                        ,process.getProcessCode()
                        ,"#272727"))
                .addData(new WxMpTemplateData("keyword2"//提交时间
                        ,new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss")
                        ,"#272727"))
                .addData(new WxMpTemplateData("keyword3"//当前审批人
                        ,currentSysUser.getName()
                        ,"#272727"))
                .addData(new WxMpTemplateData("keyword4"//根据状态是否通过设置不同颜色
                        ,status == 1 ? "审批通过" : "审批拒绝"
                        ,status == 1 ? "#009966" : "#FF0033"))
                .addData(new WxMpTemplateData("content"//内容
                        ,content.toString()
                        ,"#272727"));

        try{
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        }catch (WxErrorException e){
            throw  new RuntimeException(e);
        }
    }

    //获取具体信息,JSON数据中的值,即表单中的数据
    private StringBuffer getContent(Process process) {
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuffer content = new StringBuffer();
        for (Map.Entry entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append("：").append(entry.getValue()).append("\n ");
        }
        return content;
    }
}
