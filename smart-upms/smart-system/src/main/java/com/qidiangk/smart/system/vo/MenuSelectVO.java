package com.qidiangk.smart.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 客户端顶级菜单选项VO
 *
 * @author mr.g
 */
@Data
public class MenuSelectVO implements Serializable {

	@Schema(description = "客户端ID")
	private Long id;
	@Schema(description = "客户端名称")
	private String name;
	@Schema(description = "顶级菜单")
	private List<MenuClientVO> children;
	@Schema(description = "是否有顶级菜单")
	private Boolean hasChildren;

}
