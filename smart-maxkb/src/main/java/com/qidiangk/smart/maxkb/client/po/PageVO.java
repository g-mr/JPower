package com.qidiangk.smart.maxkb.client.po;

import lombok.Data;

import java.util.List;

/**
 * 分页实体
 *
 * @author mr.g
 */
@Data
public class PageVO<T> {

    private Integer total;
    private Integer current;
    private Integer size;
    private List<T> records;

}
