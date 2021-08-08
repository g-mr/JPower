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
package com.wlcb.jpower.module.common.support;

import cn.hutool.core.text.CharPool;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

/**
 * 字符串格式化
 *
 * @author Chill
 */
public class StrFormatter {

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： 	format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\：		format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray   参数列表
     * @return 结果
     */
    public static String format(final String strPattern, final Object... argArray) {
        if (Fc.isBlank(strPattern) || Fc.isEmpty(argArray)) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();

        /**
         * 初始化定义好的长度以获得更好的性能
         */
        StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

        /**
         * 记录已经处理到的位置
         */
        int handledPosition = 0;

        /**
         * 占位符所在位置
         */
        int delimIndex;
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            delimIndex = strPattern.indexOf(StringPool.EMPTY_JSON, handledPosition);
            /**
             * 剩余部分无占位符
             */
            if (delimIndex == -1) {
                /**
                 * 不带占位符的模板直接返回
                 */
                if (handledPosition == 0) {
                    return strPattern;
                } else {
                    sbuf.append(strPattern, handledPosition, strPatternLength);
                    return sbuf.toString();
                }
            } else {
                /**
                 * 转义符
                 */
                if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == CharPool.BACKSLASH) {
                    /**
                     * 双转义符
                     */
                    if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == CharPool.BACKSLASH) {
                        //转义符之前还有一个转义符，占位符依旧有效
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(Fc.toStr(argArray[argIndex]));
                        handledPosition = delimIndex + 2;
                    } else {
                        //占位符被转义
                        argIndex--;
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(StringPool.LEFT_BRACE);
                        handledPosition = delimIndex + 1;
                    }
                } else {//正常占位符
                    sbuf.append(strPattern, handledPosition, delimIndex);
                    sbuf.append(Fc.toStr(argArray[argIndex]));
                    handledPosition = delimIndex + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        //加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPattern.length());

        return sbuf.toString();
    }

}
