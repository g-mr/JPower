package com.qidiangk.smart.aster.pojo.vo.ivr;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VariableVo implements Serializable {

    private String label;
    private String value;
    private String type = "flow";
    private String desc;
    private String node;
    private String nodeContent;
    private List<VariableVo> children;

}
