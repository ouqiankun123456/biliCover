package com.bilibiliCover.generator.constant;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;

/**
 * 打印类型枚举类，根据不同的类型，有不同的操作
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月28日
 * @package com.bilibiliCover.generator.constant
 */
@Getter
public enum PrintTypeEnum {
	TEXT("text"), // 文字
	IMAGE("image"), // 图片
	ROUND_RECT("roundRect"), // 圆角方形
	RECT("rect"), // 方形
	GENERALPATH("generalPath"), // 自定义图片遮罩
	ROTATE("rotate"); // 旋转
	
	private String printType;
	
	PrintTypeEnum(String printType) {
		this.printType = printType;
	}
	
	public static PrintTypeEnum tran(String printType) {
		for(PrintTypeEnum p : PrintTypeEnum.values()) {
			if(StringUtils.equalsIgnoreCase(p.getPrintType(), printType))
				return p;
		}
		return null;
	}
}
