package com.qidiangk.smart.resource.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * FastDFS配置类
 * <p>
 * 导入FastDFS客户端配置并解决JMX重复注册bean的问题
 * </p>
 *
 * @author mr.g
 */
@Configuration(proxyBeanMethods = false)
@Import(FdfsClientConfig.class)
// Jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class DfsConfig {
}
