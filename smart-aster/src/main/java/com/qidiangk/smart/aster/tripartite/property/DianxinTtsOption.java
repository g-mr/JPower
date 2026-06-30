package com.qidiangk.smart.aster.tripartite.property;

import lombok.Data;

import java.io.Serializable;

/**
 * 阿里ASR配置类
 *
 * @author mr.g
 */
@Data
public class DianxinTtsOption implements Serializable {

    /**
     * appId
     */
    private String appId;
    /**
     * appKey
     */
    private String appKey;

    /**
     * ttsUrl
     */
    private String ttsUrl;

    /**
     * 说话人，支持音色 cixingnan、songchinan、surennan、cixingnan2、zhubonan、kefunan、zhuchinan、jilunan2、surennv、xianliaonv、 mansunv、kefunv1、huoponv、ruyanv、xinwennv、kefunv2、luolinv、huoponv2
     */
    private String voice;
    /**
     * 音量，取值范围：[0, 100]，默认值：50
     */
    private Integer volume;
}
