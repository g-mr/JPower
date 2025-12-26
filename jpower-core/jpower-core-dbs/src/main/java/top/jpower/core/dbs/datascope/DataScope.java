package top.jpower.core.dbs.datascope;

import lombok.Data;

import java.util.List;

/**
 * 数据权限配置
 */
@Data
public class DataScope {

    /** 权限表 **/
    private List<String> scopeTables;

    /** 权限类型 **/
    private Integer scopeType;

    /** 权限列 **/
    private String scopeColumn;

    /** 自定义值域 **/
    private String scopeValue;

}
