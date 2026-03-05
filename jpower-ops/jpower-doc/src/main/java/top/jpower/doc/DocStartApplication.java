package top.jpower.doc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.jpower.common.constants.AppConstant;
import top.jpower.core.deploy.JpowerApplication;

/**
 * Knife4jAggregation模式我发现Knife4j作者有弃用的想法，并已经推广使用Knife4jInsight模式
 * 但是Knife4jInsight模式在Knife4j生态中是更倾向于商业模式
 * 所以这里只是暂时使用Knife4jAggregation模式跑起来，后续考虑移除这个模式，采用gateway的文档聚合
 *
 * @author mr.g
 */
@Deprecated
@SpringBootApplication
public class DocStartApplication {

    public static void main(String[] args) {
        JpowerApplication.run(AppConstant.JPOWER_DOC, DocStartApplication.class,args);
    }

}
