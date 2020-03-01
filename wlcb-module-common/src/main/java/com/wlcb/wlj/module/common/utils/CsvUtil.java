package com.wlcb.wlj.module.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CsvUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-03-01 22:34
 * @Version 1.0
 */
public class CsvUtil {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

    public static List<Map<String,Object>> readCSVFileData(InputStream inputStream, List<String> filedNames,Integer startColumn,Integer endColumn,Integer startRow) throws Exception {

        BufferedReader reader = null;
        String lineDataStr = null;
        int line =1;
        List<Map<String,Object>> list = new ArrayList();


        startColumn = startColumn==null||startColumn<0?0:startColumn;
        startRow = startRow==null||startRow<1?1:startRow;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream,"GBK"));
            while ((lineDataStr = reader.readLine()) != null) {

                if (line >= startRow){
                    String[] lineDatas = lineDataStr.split(",");

                    endColumn = endColumn==null||endColumn>lineDatas.length?lineDatas.length:endColumn;

                    Map<String,Object> map = new HashMap<>();

                    int c = 0;
                    for (int i = startColumn; i < endColumn; i++) {
                        String lineData = StringUtils.trim(lineDatas[i]);
                        map.put(filedNames.get(c),lineData);
                        c++;
                    }

                    list.add(map);
                }

                line ++ ;
            }

            reader.close();
            logger.info("文件读取完成：共{}行",line);

            return list;
        } catch (FileNotFoundException e) {
            logger.error("未找到文件：{}",e.getMessage());
            throw e;
        } catch (IOException e) {
            logger.error("读取文件流出错：{}",e.getMessage());
            throw e;
        } catch (Exception e){
            logger.error("读取文件出错：{}",e.getMessage());
            throw e;
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("流关闭出错：{}",e.getMessage());
                }
            }
        }

    }
}
