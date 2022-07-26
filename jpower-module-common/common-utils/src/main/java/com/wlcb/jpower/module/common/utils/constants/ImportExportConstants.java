package com.wlcb.jpower.module.common.utils.constants;

import com.wlcb.jpower.module.common.utils.FileUtil;

import java.io.File;

/**
 * 导入导出路径
 *
 * @author mr.g
 **/
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
