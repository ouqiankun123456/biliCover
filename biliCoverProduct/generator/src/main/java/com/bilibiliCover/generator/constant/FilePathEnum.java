package com.bilibiliCover.generator.constant;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;

/**
 * 文件路径枚举类
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月23日
 * @package com.bilibiliCover.generator.constant
 */
@Getter
public enum FilePathEnum {
	TEMP("temp"),
	UPLOADFILE("uploadFile");
	
	private String path;
	
	FilePathEnum(String path) {
		this.path = path;
	}
	
	public static FilePathEnum tran(String printType) {
		for(FilePathEnum p : FilePathEnum.values()) {
			if(StringUtils.equalsIgnoreCase(p.getPath(), printType))
				return p;
		}
		return TEMP;
	}
}
