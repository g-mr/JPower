package com.qidiangk.smart.aster.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScriptIssueVO {
    private Integer line;       // 起始行 (1-based)
    private Integer column;     // 起始列 (1-based)
    private Integer endLine;    // 结束行
    private Integer endColumn;  // 结束列
    private String message; // 错误信息
    private String type; // "error", "warning", "info"
}