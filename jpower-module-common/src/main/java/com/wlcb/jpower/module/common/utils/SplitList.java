package com.wlcb.jpower.module.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mr.gmac
 */
public class SplitList {

	/**
	 * 分割List
	 * 
	 * @author bianrx
	 * @param <T>
	 * @date 2012.1.13
	 * @param list
	 *            待分割的listn
	 * @param pageSize
	 *            每段list的大小
	 * @return List<<List<T>>
	 */
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
		int listSize = list.size();// list的大小
		int page = (listSize + (pageSize - 1)) / pageSize;// 页数
		// 创建list数组 ,用来保存分割后的list
		List<List<T>> listArray = new ArrayList<List<T>>(pageSize);
		for (int i = 0; i < page; i++) { // 按照数组大小遍历
			List<T> subList = new ArrayList<T>();// 数组每一位放入一个分割后的list
			for (int j = i * pageSize; j < listSize; j++) { // 遍历待分割的list
				subList.add(list.get(j));// 放入list中的元素到分割后的list(subList)
				if ((j + 1) == ((i + 1) * pageSize)) { // 当放满一页时退出当前循环
					break;
				}
			}
			listArray.add(subList);// 将分割后的list放入对应的数组的位中
		}
		return listArray;
	}
}
