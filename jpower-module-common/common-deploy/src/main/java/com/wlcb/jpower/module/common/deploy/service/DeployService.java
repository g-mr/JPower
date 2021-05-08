/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wlcb.jpower.module.common.deploy.service;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

/**
 * @Description TODO 启动参数配置
 * @Author Chill
 */
public interface DeployService extends Ordered, Comparable<DeployService> {

    /**
     * 启动时 处理 SpringApplicationBuilder
     * @param builder SpringApplicationBuilder
     * @param appName AppName
     * @param profile Profile
     */
    void launcher(SpringApplicationBuilder builder, String appName, String profile);

    /**
     * 获取排列顺序
     * @return order
     */
    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 对比排序
     * @param o LauncherService
     * @return compare
     */
    @Override
    default int compareTo(DeployService o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }

}
