package top.jpower.jpower.module.constants;

/**
 * 数据权限常量
 *
 * @author mr.g
 */
public interface DataScopeConstant {

    /**
     * 全部
     **/
    Integer ALL = 1;
    /**
     * 本人可见
     **/
    Integer OWN = 2;
    /**
     * 所在机构可见
     **/
    Integer OWN_ORG = 3;
    /**
     * 所在机构及子级可见
     **/
    Integer OWN_ORG_CHILD = 4;
    /**
     * 自定义
     **/
    Integer CUSTOM = 5;

}
