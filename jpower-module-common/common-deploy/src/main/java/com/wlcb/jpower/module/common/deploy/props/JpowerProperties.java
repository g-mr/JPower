package com.wlcb.jpower.module.common.deploy.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件
 * @author mr.g
 */
@ConfigurationProperties("jpower")
@Data
public class JpowerProperties {

	/**
	 * 运行环境
	 */
	private String env;

	/**
	 * 服务名
	 */
	private String name;

	/**
	 * 判断是否为 本地开发环境
	 */
	private Boolean isLocal = Boolean.FALSE;

	/**
	 * hostName
	 */
	private String hostName;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 端口
	 */
	private Integer port;

}
