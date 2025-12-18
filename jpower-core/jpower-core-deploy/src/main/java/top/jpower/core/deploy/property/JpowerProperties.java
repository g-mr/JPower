package top.jpower.core.deploy.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件
 * @author mr.g
 */
@ConfigurationProperties(prefix = "jpower")
@Data
public class JpowerProperties {

	/**
	 * 运行环境
	 */
	private String env;
	/**
	 * 服务类型 (微服务：cloud、单体服务：boot)
	 *
	 * TODO 回头想想有什么办法可以自动判断不需要配置
	 */
	private SERVER server = SERVER.BOOT;
	/**
	 * 服务名
	 */
	private String applicationName;

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

	/**
	 * 主包名
	 */
	private String mainPackages;

	public enum SERVER {
		CLOUD,BOOT
	}

}
