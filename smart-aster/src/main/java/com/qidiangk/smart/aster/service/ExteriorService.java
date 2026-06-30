package com.qidiangk.smart.aster.service;

import com.qidiangk.smart.aster.pojo.dto.ActionDTO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallEndDTO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallStartDTO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallStatusDTO;

/**
 * 外部服务通知
 *
 * @author mr.g
 */
public interface ExteriorService {

    /**
     * 通话开始通知
     * @param startDTO 数据
     */
    void callReport(NoticeCallStartDTO startDTO);

    /**
     * 通话状态变更
     * @param callStatusDTO 状态数据
     */
    void callStatus(NoticeCallStatusDTO callStatusDTO);

    /**
     * 挂断上报
     */
    void hangupReport(NoticeCallEndDTO huangDTO);

    /**
     * 实时数据
     * @param actionDTO 对话内容
     */
    void sendAction(ActionDTO actionDTO);

    /**
     * 录音文件上传
     * @param linkedId 通话状态
     * @param filepath 文件路径
     */
    void uploadFile(String linkedId, String filepath);
}
