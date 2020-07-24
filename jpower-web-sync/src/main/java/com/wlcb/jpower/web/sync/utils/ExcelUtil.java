package com.wlcb.jpower.web.sync.utils;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExcelUtil
 * @Description TODO excel操作类
 * @Author 郭丁志
 * @Date 2020-04-03 10:25
 * @Version 1.0
 */
public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * @Author 郭丁志
     * @Description //TODO 把excel报表转换成list信息,开始行不传默认是第2行，结束行不传默认是最后一行
     * @Date 10:34 2020-04-03
     * @Param [path, startRow 开始行, endRow 结束行]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    public static List<Map<String,Object>> excel2List(String path,Integer startRow,Integer endRow){

        File file=new File(path);
        if(!file.exists()){
            logger.info("文件不存在，file={}",file.getAbsolutePath());
            return null;
        }

        Workbook wb = getExcel(path);
        if(wb==null) {
            return null;
        } else {

            return analyzeExcel(wb,(startRow == null?1:startRow),endRow);
        }
    }
    public static List<Map<String,Object>> excel2List(String path){

        File file=new File(path);
        if(!file.exists()){
            logger.info("文件不存在，file={}",file.getAbsolutePath());
            return null;
        }

        Workbook wb = getExcel(path);
        if(wb==null) {
            return null;
        } else {

            return analyzeExcel(wb,1,null);
        }
    }

    private static List<Map<String, Object>> analyzeExcel(Workbook wb, Integer startRow, Integer endRow){
        //读取第一个sheet
        List<Map<String, Object>> list = new ArrayList<>();
        Sheet sheet=wb.getSheetAt(0);

        //获取头列
        Row headerRow = sheet.getRow(0);

        String[] headerText = new String[headerRow.getLastCellNum()];
        for (int i = 0; i <headerRow.getLastCellNum(); i++) {
            if (headerRow.getCell(i) == null){
                headerText[i] = i+"";
            }else {
                headerText[i] = Fc.trim(headerRow.getCell(i).toString());
            }
        }

        int rowNum=endRow==null?sheet.getLastRowNum():endRow;
        for(int i=startRow;i<=rowNum;i++){

            Map<String,Object> map = new HashMap<>();

            //获得行
            Row row=sheet.getRow(i);
            //获得要读取的结束列
//            int colNum=row.getLastCellNum();
            if (row!=null){
                for(int j=0;j<headerText.length;j++){
                    //获取单元格
                    Cell cell=row.getCell(j);
                    if(cell==null) {
                        if (StringUtils.isNotBlank(headerText[j])){
                            map.put(headerText[j],"");
                        }else {
                            map.put(j+"","");
                        }
                    } else {
                        if (StringUtils.isNotBlank(headerText[j])){
                            map.put(headerText[j],Fc.trim(cell.toString()));
                        }else {
                            map.put(j+"",Fc.trim(cell.toString()));
                        }
                    }
                }

                list.add(map);
            }
        }
        return list;
    }

    public static Workbook getExcel(String filePath){
        Workbook wb=null;
        String fileType=filePath.substring(filePath.lastIndexOf("."));//获得后缀名
        File file = new File(filePath);
        try {
            if(".xls".equals(fileType)){
                if (!file.exists()){
                    wb = new HSSFWorkbook();
                }else {
                    wb = new HSSFWorkbook(new FileInputStream(filePath));
                }

            }else if(".xlsx".equals(fileType)){
                if (!file.exists()){
                    wb = new XSSFWorkbook();
                }else {
                    wb = new XSSFWorkbook(new FileInputStream(filePath));
                }
            }else{
                logger.info("不是excel文件,文件名={}",filePath);
                wb=null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return wb;
    }

    public static void writer(String path, String fileName,String fileType,List<Map<String,Object>> list,List<String> titleRow) throws Exception {
        writer(path, fileName, null, fileType, list, titleRow);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 生成Ecel
     * @Date 01:08 2020-04-06
     * @Param [path, fileName, fileType, list, titleRow]
     * @return void
     **/
    public static void writer(String path, String fileName,String sheetName,String fileType,List<Map<String,Object>> list,List<String> titleRow) throws Exception {
        String excelPath = path+File.separator+fileName+"."+fileType;
        File file = new File(excelPath);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        Workbook wb = getExcel(excelPath);
        if (wb == null){
            logger.error("未获取到文件，path={}",file.getAbsolutePath());
            throw new Exception("未获取到文件");
        }

        Sheet sheet = wb.getSheet("sheet1");
        if (StringUtils.isBlank(sheetName)){
            if (wb.getNumberOfSheets() > 0){
                sheet = wb.getSheetAt(0);
            }
        }else {
            sheet = wb.getSheet(sheetName);
        }

        //创建工作文档对象
        if (!file.exists()) {
            //创建sheet对象
            sheet = wb.createSheet(StringUtils.isBlank(sheetName)?"sheet1":sheetName);
            OutputStream outputStream = new FileOutputStream(excelPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();

        }
        //创建sheet对象
        if (sheet==null) {
            sheet = wb.createSheet(StringUtils.isBlank(sheetName)?"sheet1":sheetName);
        }
        sheet.setSelected(true);

        // 表头样式对象
        CellStyle style = wb.createCellStyle();
        // 垂直
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平
        style.setAlignment(HorizontalAlignment.CENTER);

        // 错误数据样式对象
        CellStyle styleRed = wb.createCellStyle();
        // 垂直
        styleRed.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平
        styleRed.setAlignment(HorizontalAlignment.CENTER);
        // 背景红色
        styleRed.setFillForegroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        styleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 数据样式对象
        CellStyle styleData = wb.createCellStyle();
        // 垂直
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平
        styleData.setAlignment(HorizontalAlignment.CENTER);

        //表头字体
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        style.setFont(font);
        //添加表头
        //创建第一行
        Row row = sheet.createRow(0);
        for(int j = 0;j < titleRow.size();j++){
            Cell cell = row.createCell(j);
            cell.setCellValue(titleRow.get(j));
            // 样式，居中
            cell.setCellStyle(style);
            sheet.setColumnWidth(j, 20 * 256);
        }
        row.setHeight((short) 540);

        //循环写入行数据
        for (int i = 0; i < list.size(); i++) {
            Map<String,Object> map = list.get(i);

            Row rowData = sheet.createRow(i+1);

            for(int j = 0;j < titleRow.size();j++){
                Cell cellData = rowData.createCell(j);

                String str = Fc.toStr(map.get(titleRow.get(j)));
                if (str.contains(":red")){
                    cellData.setCellValue(str.split(":")[0]);
                    cellData.setCellStyle(styleRed);
                }else {
                    cellData.setCellValue(str);
                    cellData.setCellStyle(styleData);
                }
            }
        }

        //创建文件流
        OutputStream stream = new FileOutputStream(excelPath);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();
    }

    public static void main(String[] args) {
        List<Map<String,Object>> list = excel2List("/Users/mr.gmac/Desktop/111.xlsx",null,null);

        for (Map<String, Object> stringStringMap : list) {

            System.out.println(JSON.toJSONString(stringStringMap));

        }
    }

}
