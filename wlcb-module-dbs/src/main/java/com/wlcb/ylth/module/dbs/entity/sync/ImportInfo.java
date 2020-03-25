package com.wlcb.ylth.module.dbs.entity.sync;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @ClassName ImportInfo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-03-01 22:20
 * @Version 1.0
 */
@Data
public class ImportInfo implements Serializable {

    private static final long serialVersionUID = 155262414122364884L;

    //导入文件
    MultipartFile file;
    //导入字段
    String fileds;
    //每批次数量
    Integer limit = 2000;
    //导入表名
    String tableName;
    //开始列 下标从0开始
    Integer startColumn;
    //结束列 下标从0开始 不填默认是到最后一列
    Integer endColumn;
    //开始行 下标从1开始
    Integer startRow;
}
