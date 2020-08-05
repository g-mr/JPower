
package com.wlcb.jpower.module.base.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 配置文件读取
 * @author mr.gmac
 */
public class SysProperties {

    private final Logger logger = LoggerFactory.getLogger(SysProperties.class);

    private static SysProperties configuration = null;
    private String PROD_FILE_PATH = "sysConfig.properties";
    Properties prop = null;
    InputStream inputStream = null;

    private SysProperties() {
        readConfig();
    }

    public static SysProperties getInstance(){
        if(configuration == null){
            configuration = new SysProperties();
        }
        return configuration;
    }

    public Properties getProp() {
      return prop;
    }

    public String getProperties(String para){
        return (String)prop.get(para);
    }

    public String getProperties(String para,String defVal){
        if (prop.get(para) == null || prop.get(para) == ""){
            return defVal;
        }
        return (String)prop.get(para);
    }

    private void readConfig(){
        try{
            prop = new Properties();
            File file = new File(getRootPath()+File.separator+PROD_FILE_PATH);
            if (file.exists()){
                inputStream = new FileInputStream(file);
                prop.load(inputStream);
            }else {
                logger.warn("未查找到sysConfig.properties配置文件");
            }
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try{
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 如果已打成jar包，则返回jar包所在目录
     * 如果未打成jar，则返回resources所在目录
     * @return
     */
    private String getRootPath() throws UnsupportedEncodingException {
        // 项目的编译文件的根目录
        String path = URLDecoder.decode(this.getClass().getResource("/").getPath(), String.valueOf(StandardCharsets.UTF_8));
        if (path.startsWith("file:")) {
            int i = path.indexOf(".jar!");
            path = path.substring(0, i);
            path = path.replaceFirst("file:", "");
            path = new File(path).getParentFile().getAbsolutePath();
            path = path + File.separator + "config";
        }
        // 项目所在的目录
        return new File(path).getAbsolutePath();
    }
}
