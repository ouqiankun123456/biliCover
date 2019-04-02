package com.bilibiliCover.generator.utils;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * 用于替换字符串变量的工具类
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月9日
 * @package com.bilibiliCover.generator.utils
 */
public class ParseParams {
	
	private static final String PREFIX = "${";
	private static final String SUFFIX = "}";
	
	/**
	 * 根据prefix和suffix结合params替换掉str中与params中key对应的value
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月9日
	 * @return String
	 * @param str
	 * @param params
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static String parseParams(String str, HashMap<String, Object> params, String prefix, String suffix) {
		
		StringBuilder sb = new StringBuilder(str);
		
		params.forEach((key, value) ->{
			String param = prefix + key + suffix;
			String result = sb.toString().replaceAll(escapeExprSpecialWord(param), escapeExprSpecialWord(value.toString()));
			sb.delete(0, sb.length());
			sb.append(result);
		});
		return sb.toString();
	}
	
	public static String parseParams(String str, HashMap<String, Object> params) {
		return parseParams(str, params, PREFIX, SUFFIX);
	}
	
	/**
	 * 转义正则特殊字符 （$()*+.[]?\^{},|）
	 * 
	 * @param keyword
	 * @return
	 */
	public static String escapeExprSpecialWord(String keyword) {
		if (StringUtils.isNotBlank(keyword)) {
			String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
			for (String key : fbsArr) {
				if (keyword.contains(key)) {
					keyword = keyword.replace(key, "\\" + key);
				}
			}
		}
		return keyword;
	}
	
	public static String escapeExprSpecialWord(String keyword, String symbol) {
		keyword = keyword.replace(symbol, "\\" + symbol);
		return keyword;
	}


}
