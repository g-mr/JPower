package top.jpower.core.dbs.datascope.constants;

/**
 * 数据权限常量
 *
 * @author mr.g
 */
public interface DataScopeConstant {

    /**
     * 全部
     **/
    int ALL = 1;
    /**
     * 本人可见
     **/
    int OWN = 2;
    /**
     * 所在机构可见
     **/
    int OWN_ORG = 3;
    /**
     * 所在机构及子级可见
     **/
    int OWN_ORG_CHILD = 4;
    /**
     * 自定义
     **/
    int CUSTOM = 5;

}
