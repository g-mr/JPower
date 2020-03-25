package com.wlcb.ylth.module.common.utils;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;

/**
 * @ClassName ImageUtils
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-03-09 00:55
 * @Version 1.0
 */
public class ImageUtils {

    private String path;
    private String imgPath;
    private String[] shuju;
    private String number;
    private String outPath;


    /**
     * @Author 郭丁志
     * @Description //TODO
     * @Date 13:34 2020-03-11
     * @Param [path 底图路径, imgPath 二维码路径, shuju 输出数据, number 编号, outPath 输出文件全路径]
     * @return
     **/
    public ImageUtils(String path,String imgPath,String[] shuju,String number,String outPath){
        this.path = path;
        this.imgPath = imgPath;
        this.shuju = shuju;
        this.number = number;
        this.outPath = outPath;
    }

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
//        exportImg1();
        new ImageUtils("/Users/mr.gmac/Documents/耀灵公司文档/ceshi/12312.jpg","/Users/mr.gmac/Documents/耀灵公司文档/ceshi/111.jpeg",new String[]{"乌兰花镇","王府路社区","四子王旗公安局桥东派出所"},"12345","out/").exportImg();
    }


    public void exportImg(){
        try {
            //1.jpg是你的 主图片的路径
            InputStream is = new FileInputStream(path);


            //通过JPEG图象流创建JPEG数据流解码器
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);

            //解码当前JPEG数据流,改变图片大小，返回BufferedImage对象
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();

            //得到画笔对象
            Graphics g = buffImg.getGraphics();

            //创建你要附加的图象。
            //小图片的路径
            ImageIcon imgIcon = new ImageIcon(imgPath);

            //得到Image对象。
            Image img = imgIcon.getImage();

            //将小图片绘到大图片上。
            g.drawImage(img,(buffImg.getWidth()-imgIcon.getIconWidth())/2, 100,null);

            //设置颜色。
            g.setColor(Color.BLACK);

            //最后一个参数用来设置字体的大小
            Font f = new Font("宋体",Font.PLAIN,50);
            Color mycolor = Color.BLACK;//new Color(0, 0, 255);
            g.setColor(mycolor);
            g.setFont(f);

            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
            int i = 0 ;
            for (String s : shuju) {
                g.drawString(s,100,555+i);
                i = i + 75;
            }

            g.drawString(number,imgIcon.getIconWidth()+((buffImg.getWidth()-imgIcon.getIconWidth())/2)+35,470);

            g.dispose();

            String shareFileName = outPath;
            OutputStream os = new FileOutputStream(shareFileName);
            //创键编码器，用于编码内存中的图象数据。
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            en.encode(buffImg);

            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ImageFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
