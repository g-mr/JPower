package top.jpower.core.dbs.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * 租户工具类
 *
 * @author mr.g
 */
@UtilityClass
public class TenantContextHolder {

	private final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

	private final ThreadLocal<Boolean> THREAD_LOCAL_TENANT_SKIP_FLAG = new TransmittableThreadLocal<>();

	/**
	 * TTL 设置租户ID<br/>
	 * <b>谨慎使用此方法,避免嵌套调用。尽量使用 {@code TenantBroker} </b>
	 * @param tenantCode
	 * @see TenantBroker
	 */
	public void setTenantCode(String tenantCode) {
		THREAD_LOCAL_TENANT.set(tenantCode);
	}

	/**
	 * 设置是否过滤的标识
	 */
	public void setTenantSkip() {
		THREAD_LOCAL_TENANT_SKIP_FLAG.set(Boolean.TRUE);
	}

	/**
	 * 清除当前线程的租户跳过标记
	 */
	public void unsetTenantSkip() {
		THREAD_LOCAL_TENANT_SKIP_FLAG.remove();
	}

	/**
	 * 获取TTL中的租户ID
	 * @return
	 */
	public String getTenantCode() {
		return THREAD_LOCAL_TENANT.get();
	}

	/**
	 * 获取是否跳过租户过滤的标识
	 * @return
	 */
	public Boolean getTenantSkip() {
		return THREAD_LOCAL_TENANT_SKIP_FLAG.get() != null && THREAD_LOCAL_TENANT_SKIP_FLAG.get();
	}

	public void clear() {
		THREAD_LOCAL_TENANT.remove();
		THREAD_LOCAL_TENANT_SKIP_FLAG.remove();
	}

}
