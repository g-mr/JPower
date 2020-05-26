package com.wlcb.jpower.utils.excel;

import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.base.annotation.Excel;
import com.wlcb.jpower.module.common.utils.Convert;
import com.wlcb.jpower.module.common.utils.DateUtils;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsReturn;
import com.wlcb.jpower.module.common.utils.reflect.ReflectUtils;
import com.wlcb.jpower.web.sync.utils.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName BeanExcelUtil
 * @Description TODO 把并导出excel
 * @Author 郭丁志
 * @Date 2020-04-11 20:58
 * @Version 1.0
 */
public class BeanExcelUtil<T> {

    private static final Logger log = LoggerFactory.getLogger(BeanExcelUtil.class);

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Excel.Type type;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 导入导出数据列表
     */
    private List<T> list;

    /**
     * 注解列表
     */
    private List<Field> fields;

    /**
     * 实体对象
     */
    public Class<T> clazz;

    /**
     * 下载文件地址
     **/
    public String downloadPath;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    public BeanExcelUtil(Class<T> clazz,String downloadPath){
        this.clazz = clazz;
        this.downloadPath = downloadPath;
    }

    public BeanExcelUtil(Class<T> clazz){
        this.clazz = clazz;
    }


    public void init(List<T> list, String sheetName, Excel.Type type)
    {
        if (list == null)
        {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public ResponseData exportExcel(List<T> list, String sheetName)
    {
        this.init(list, sheetName, Excel.Type.EXPORT);
        return exportExcel();
    }

    /**
     * 创建一个工作簿
     */
    public void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField()
    {
        this.fields = new ArrayList<Field>();
        List<Field> tempFields = new ArrayList<>();
        Class<?> tempClass = clazz;
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        while (tempClass != null)
        {
            tempClass = tempClass.getSuperclass();
            if (tempClass != null)
            {
                tempFields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            }
        }
        putToFields(tempFields);
    }

    /**
     * 放到字段集合中
     */
    private void putToFields(List<Field> fields)
    {
        for (Field field : fields)
        {
            Excel attr = field.getAnnotation(Excel.class);
            if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
            {
                this.fields.add(field);
            }
        }
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @return 结果
     */
    public ResponseData exportExcel(){
        log.info("开始生成文件");
        OutputStream out = null;
        try
        {
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(list.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++)
            {
                createSheet(sheetNo, index);
                Cell cell = null; // 产生单元格

                // 产生一行
                Row row = sheet.createRow(0);
                log.info("开始写入数据");
                // 写入各个字段的列头名称
                for (int i = 0; i < fields.size(); i++)
                {
                    Field field = fields.get(i);
                    Excel attr = field.getAnnotation(Excel.class);
                    // 创建列
                    cell = row.createCell(i);
                    // 设置列中写入内容为String类型
                    cell.setCellType(CellType.STRING);
                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    if (attr.name().indexOf("注：") >= 0)
                    {
                        Font font = wb.createFont();
                        font.setColor(HSSFFont.COLOR_RED);
                        cellStyle.setFont(font);
                        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
                        sheet.setColumnWidth(i, 6000);
                    }
                    else
                    {
                        Font font = wb.createFont();
                        // 粗体显示
                        font.setBold(true);
                        // 选择需要用到的字体格式
                        cellStyle.setFont(font);
                        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
                        // 设置列宽
                        sheet.setColumnWidth(i, (int) ((attr.width() + 0.72) * 256));
                        row.setHeight((short) (attr.height() * 20));
                    }
                    log.info("结束写入数据");
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyle.setWrapText(true);
                    cell.setCellStyle(cellStyle);

                    // 写入列名
                    cell.setCellValue(attr.name());
                    log.info("===================");
                    // 如果设置了提示信息则鼠标放上去提示.
                    if (StringUtils.isNotEmpty(attr.prompt()))
                    {
                        // 这里默认设了2-101列提示.
                        setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, i, i);
                    }
                    // 如果设置了combo属性则本列只能选择不能输入
                    if (attr.combo().length > 0)
                    {
                        // 这里默认设了2-101列只能选择不能输入.
                        setXSSFValidation(sheet, attr.combo(), 1, 100, i, i);
                    }
                }
                log.info("=========---------==========");
                if (Excel.Type.EXPORT.equals(type))
                {
                    fillExcelData(index, row, cell);
                }
            }
            String filename = encodingFilename(sheetName);
            String path = getAbsoluteFile(filename);
            log.info("文件生成路径={}",path);
            out = new FileOutputStream(path);
            wb.write(out);
            return ReturnJsonUtil.printJson(ConstantsReturn.RECODE_SUCCESS,"生成成功",filename,true);
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
        finally
        {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = this.downloadPath + File.separator + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()){
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename)
    {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row 单元格行
     * @param cell 类型单元格
     */
    public void fillExcelData(int index, Row row, Cell cell)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        // 写入各条记录,每条记录对应excel表中的一行
        CellStyle cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(i + 1 - startNo);
            // 得到导出对象.
            T vo = (T) list.get(i);
            for (int j = 0; j < fields.size(); j++)
            {
                // 获得field.
                Field field = fields.get(j);
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                Excel attr = field.getAnnotation(Excel.class);
                try
                {
                    // 设置行高
                    row.setHeight((short) (attr.height() * 20));
                    // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                    if (attr.isExport())
                    {
                        // 创建cell
                        cell = row.createCell(j);
                        cell.setCellStyle(cs);
                        if (vo == null)
                        {
                            // 如果数据存在就填入,不存在填入空格.
                            cell.setCellValue("");
                            continue;
                        }

                        // 用于读取对象中的属性
                        Object value = getTargetValue(vo, field, attr);
                        String dateFormat = attr.dateFormat();
                        String readConverterExp = attr.readConverterExp();
                        if (StringUtils.isNotEmpty(dateFormat) && value != null)
                        {
                            cell.setCellValue(DateUtils.getDate((Date) value,dateFormat));
                        }
                        else if (StringUtils.isNotEmpty(readConverterExp) && value != null)
                        {
                            cell.setCellValue(convertByExp(String.valueOf(value), readConverterExp));
                        }
                        else
                        {
                            cell.setCellType(CellType.STRING);
                            // 如果数据存在就填入,不存在填入空格.
                            cell.setCellValue(value == null ? attr.defaultValue() : value + attr.suffix());
                        }
                    }
                }
                catch (Exception e)
                {
                    log.error("导出Excel失败{}", e);
                }
            }
        }
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @return 解析后值
     * @throws Exception
     */
    public static String convertByExp(String propertyValue, String converterExp) throws Exception
    {
        try
        {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource)
            {
                String[] itemArray = item.split("=");
                if (itemArray[0].equals(propertyValue))
                {
                    return itemArray[1];
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return propertyValue;
    }

    /**
     * 获取bean中的属性值
     *
     * @param vo 实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception
    {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr()))
        {
            String target = excel.targetAttr();
            if (target.indexOf(".") > -1)
            {
                String[] targets = target.split("[.]");
                for (String name : targets)
                {
                    o = getValue(o, name);
                }
            }
            else
            {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法方法形式获取值
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception
    {
        if (StringUtils.isNotEmpty(name))
        {
            Class<?> clazz = o.getClass();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName);
            o = method.invoke(o);
        }
        return o;
    }

    /**
     * 设置 POI XSSFSheet 单元格提示
     *
     * @param sheet 表单
     * @param promptTitle 提示标题
     * @param promptContent 提示内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     */
    public void setXSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
                              int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet 要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    public void setXSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation)
        {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }
        else
        {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
    }

    /**
     * 创建工作表
     *
     * @param sheetNo sheet数量
     * @param index 序号
     */
    public void createSheet(double sheetNo, int index)
    {
        this.sheet = wb.createSheet();
        // 设置工作表的名称.
        if (sheetNo == 0)
        {
            wb.setSheetName(index, sheetName);
        }
        else
        {
            wb.setSheetName(index, sheetName + index);
        }
    }

    public List<T> importExcel(File file) throws Exception
    {
        return importExcel(null,file);
    }

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param file 文件
     * @return 转换后集合
     */
    public List<T> importExcel(String sheetName, File file) throws Exception {

        if (!file.exists()){
            throw new BusinessException("文件不存在,"+file.getAbsolutePath());
        }

        this.type = Excel.Type.IMPORT;
//        this.wb = WorkbookFactory.create(is);
        this.wb = ExcelUtil.getExcel(file.getAbsolutePath());
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new IOException("文件sheet不存在");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows > 0)
        {
            // 定义一个map用于存放excel列的序号和field.
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // 获取表头
            Row heard = sheet.getRow(0);
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = heard.getCell(i);
                if (cell != null)
                {
                    String value = this.getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                }
                else
                {
                    cellMap.put(null, i);
                }
            }

            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            for (int col = 0; col < allFields.length; col++)
            {
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
                {
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    Integer column = cellMap.get(attr.name());
                    fieldsMap.put(column, field);
                }
            }
            for (int i = 1; i < rows; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                T entity = null;
                for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet()){
                    if (entry.getKey() != null){
                        Object val = this.getCellValue(row, entry.getKey());

                        // 如果不存在实例则新建.
                        entity = (entity == null ? clazz.newInstance() : entity);
                        // 从map中得到对应列的field.
                        Field field = fieldsMap.get(entry.getKey());
                        // 取得类型,并根据对象类型设置值.
                        Class<?> fieldType = field.getType();
                        if (String.class == fieldType)
                        {
                            String s = Convert.toStr(val);
                            if (StringUtils.endsWith(s, ".0"))
                            {
                                val = StringUtils.substringBefore(s, ".0");
                            }
                            else
                            {
                                val = Convert.toStr(val);
                            }
                        }
                        else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType))
                        {
                            val = Convert.toInt(val);
                        }
                        else if ((Long.TYPE == fieldType) || (Long.class == fieldType))
                        {
                            val = Convert.toLong(val);
                        }
                        else if ((Double.TYPE == fieldType) || (Double.class == fieldType))
                        {
                            val = Convert.toDouble(val);
                        }
                        else if ((Float.TYPE == fieldType) || (Float.class == fieldType))
                        {
                            val = Convert.toFloat(val);
                        }
                        else if (BigDecimal.class == fieldType)
                        {
                            val = Convert.toBigDecimal(val);
                        }
                        else if (Date.class == fieldType)
                        {
                            if (val instanceof String)
                            {
                                val = DateUtils.parseDate(val);
                            }
                            else if (val instanceof Double)
                            {
                                val = DateUtil.getJavaDate((Double) val);
                            }
                        }
                        if (fieldType != null)
                        {
                            Excel attr = field.getAnnotation(Excel.class);
                            String propertyName = field.getName();
                            if (StringUtils.isNotEmpty(attr.targetAttr()))
                            {
                                propertyName = field.getName() + "." + attr.targetAttr();
                            }
                            if (StringUtils.isNotEmpty(attr.readConverterExp()))
                            {
                                val = reverseByExp(String.valueOf(val), attr.readConverterExp());
                            }
                            ReflectUtils.invokeSetter(entity, propertyName, val);
                        }
                    }
                }
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @return 解析后值
     * @throws Exception
     */
    public static String reverseByExp(String propertyValue, String converterExp) throws Exception
    {
        try
        {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource)
            {
                String[] itemArray = item.split("=");
                if (itemArray[1].equals(propertyValue))
                {
                    return itemArray[0];
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return propertyValue;
    }

    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (cell != null)
            {
                if (cell.getCellTypeEnum() == CellType.NUMERIC)
                {
                    val = cell.getNumericCellValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell))
                    {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                    }
                    else
                    {
                        if ((Double) val % 1 > 0)
                        {
                            val = new DecimalFormat("0.00").format(val);
                        }
                        else
                        {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }
                else if (cell.getCellTypeEnum() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellTypeEnum() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }
}
