package com.bilibiliCover.generator.constant;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;

/**
 * 输出封面尺寸枚举类
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月3日
 * @package com.bilibiliCover.generator.constant
 */
@Getter
public enum PrintSizeEnum {
	/** 预览图尺寸*/
	PREVIEW_IMAGE(320, 200, "previewImage"),
	/** bilibili*/
	BILIBILI(960, 600, "bilibili");
	
	
	private Integer width;
	private Integer height;
	private String name;
	
	PrintSizeEnum(Integer width, Integer height, String name){
		this.width = width;
		this.height = height;
		this.name = name;
	}
	
	public static PrintSizeEnum tran(String printType) {
		for(PrintSizeEnum p : PrintSizeEnum.values()) {
			if(StringUtils.equalsIgnoreCase(p.getName(), printType))
				return p;
		}
		return BILIBILI;
	}
	
}
