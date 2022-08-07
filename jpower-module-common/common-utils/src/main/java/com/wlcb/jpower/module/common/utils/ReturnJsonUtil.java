package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.base.vo.Pg;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.support.EnvBeanUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;

/**
 * 返回封装工具
 *
 * @author mr.g
 **/
public class ReturnJsonUtil {

    /**
     * MybatisPlus的包路径
     **/
    private static final String MP_PACKAGE = "com.baomidou.mybatisplus";
    /**
     * PageHelper的包路径
     **/
    private static final String PH_PACKAGE = "com.github.pagehelper";

    /**
     * 封装
     *
     * @author mr.g
     * @param code 结果码
     * @param msg 返回信息
     * @param data 返回数据
     * @param status 返回状态
     * @return 返回实体
     **/
    public static <T> ResponseData<T> print(Integer code, String msg, T data, boolean status){
        return ResponseData.<T>builder().data(data).code(code).message(msg).status(status).build();
    }

    /**
     * 封装
     *
     * @author mr.g
     * @param code 结果码
     * @param msg 返回信息
     * @param status 返回状态
     * @return 返回实体
     **/
    public static <T> ResponseData<T> print(Integer code,String msg,boolean status){
        return ResponseData.<T>builder().data(null).code(code).message(msg).status(status).build();
    }

    /**
     * 成功的数据结果封装
     *
     * @author mr.g
     * @param data 返回数据
     * @return 返回实体
     **/
    public static <T> ResponseData data(T data){
        return ok("成功", data);
    }

    /**
     * 成功的数据结果封装
     *
     * @author mr.g
     * @param msg 返回信息
     * @param data 返回数据
     * @return 返回实体
     **/
    public static <T> ResponseData ok(String msg, T data){
        if (Fc.isNull(data)){
            return print(ConstantsReturn.RECODE_SUCCESS, msg, data, true);
        }

        if (!ClassUtil.isSimpleValueType(data.getClass())){
            if (StringUtil.startWith(data.getClass().getName(),MP_PACKAGE)){
                return print(ConstantsReturn.RECODE_SUCCESS, msg, new Pg<>(ReflectUtil.invoke(data,"getTotal"),ReflectUtil.invoke(data,"getRecords")), true);
            }

            if (StringUtil.startWith(data.getClass().getName(),PH_PACKAGE)){
                return ReturnJsonUtil.print(ConstantsReturn.RECODE_SUCCESS, msg, new Pg<>(ReflectUtil.invoke(data,"getTotal"),ReflectUtil.invoke(data,"getList")), true);
            }

        }
        return print(ConstantsReturn.RECODE_SUCCESS, msg, data, true);
    }

    /**
     * 成功的结果封装
     *
     * @author mr.g
     * @param msg 返回信息
     * @return 返回实体
     **/
    public static <T> ResponseData<T> ok(String msg){
        return print(ConstantsReturn.RECODE_SUCCESS, msg, null, true);
    }

    /**
     * 结果封装
     *
     * @author mr.g
     * @param is 是否成功
     * @return 返回实体
     **/
    public static <T> ResponseData<T> status(Boolean is) {
        return status(is,null);
    }

    public static <T> ResponseData<T> status(Boolean is, T data) {
        if(is){
            return ok("操作成功", data);
        }else {
            if (EnvBeanUtil.getDemoEnable()){
                return fail("演示环境不支持操作！");
            }
            return fail("操作失败", data);
        }
    }

    /**
     * 失败的结果封装
     *
     * @author mr.g
     * @param msg 返回信息
     * @param data 返回数据
     * @return 返回实体
     **/
    public static <T> ResponseData<T> fail(String msg, T data){
        return print(ConstantsReturn.RECODE_FAIL, msg, data, false);
    }

    /**
     * 失败的结果封装
     *
     * @author mr.g
     * @param msg 返回信息
     * @return 返回实体
     **/
    public static <T> ResponseData<T> fail(String msg){
        return fail( msg, null);
    }

    /**
     * 失败的结果封装
     *
     * @author mr.g
     * @param code 结果码
     * @param msg 返回信息
     * @return 返回实体
     **/
    public static <T> ResponseData<T> fail(Integer code, String msg){
        return print(code, msg, null, false);
    }

    /**
     * 未找到得结果封装
     *
     * @author mr.g
     * @param msg 返回信息
     * @return 返回实体
     **/
    public static <T> ResponseData<T> notFind(String msg){
        return print(ConstantsReturn.RECODE_NOTFOUND, msg, false);
    }

    /**
     * 业务失败结果封装
     *
     * @author mr.g
     * @param msg 返回消息
     * @return 返回实体
     **/
    public static <T> ResponseData<T> busFail(String msg) {
        return print(ConstantsReturn.RECODE_BUSINESS,msg,false);
    }

}
