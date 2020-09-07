package com.wlcb.jpower.module.common.utils.constants;

import com.wlcb.jpower.module.common.utils.FileUtil;

import java.io.File;

/**
 * @ClassName ImportExportConstants
 * @Description TODO 导入导出
 * @Author 郭丁志
 * @Date 2020-08-21 11:06
 * @Version 1.0
 */
public class ImportExportConstants {

    /** 导入文件夹 **/
    public static final String IMPORT_FOLDER = "import";

    /** 导出文件夹 **/
    public static final String EXPORT_FOLDER = "download";

    /** 导出模板文件夹 **/
    public static final String EXPORT_TEMPLATE_FOLDER = "download";

    /** 导入路径 **/
    public static final String IMPORT_PATH = FileUtil.getSysRootPath() + File.separator + IMPORT_FOLDER + File.separator;

    /** 导出路径 **/
    public static final String EXPORT_PATH = FileUtil.getSysRootPath() + File.separator + EXPORT_FOLDER + File.separator;

    /** 模板导出路径 **/
    public static final String EXPORT_TEMPLATE_PATH = FileUtil.getSysRootPath() + File.separator + EXPORT_TEMPLATE_FOLDER + File.separator;

}
