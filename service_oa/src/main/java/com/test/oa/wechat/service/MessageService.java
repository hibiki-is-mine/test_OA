package com.test.oa.wechat.service;

/**
 * @return
 */
public interface MessageService {


    /**
     * 将消息推送给审批人
     *
     * @param processId process id
     * @param userId    user id
     * @param taskId    task id
     */
    void pushPendingMessage(Long processId,Long userId,String taskId);

    /**
     * 审批完成后推送消息给提交人
     *
     * @param processId process id
     * @param userId    user id
     * @param status    status
     */
    void pushProcessedMessage(Long processId, Long userId, Integer status);
}

