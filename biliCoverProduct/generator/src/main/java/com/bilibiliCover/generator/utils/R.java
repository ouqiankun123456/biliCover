package com.bilibiliCover.generator.utils;

import java.util.HashMap;

/**
 * 
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月29日
 * @package com.bilibiliCover.generator.utils
 */
public class R extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "code";
	public static final String RESULT = "result";
	public static final String MESSAGE = "message";
	
	public R() {
	}
	
	public static R success() {
		R r = new R();
		r.put(CODE, 200);
		return r;
	}
	
	public static R success(String message) {
		R r = new R();
		r.put(CODE, 200);
		r.put(MESSAGE, message);
		return r;
	}
	
	public static R error() {
		R r = new R();
		r.put(CODE, 500);
		return r;
	}
	
	public static R error(String message) {
		R r = new R();
		r.put(CODE, 500);
		r.put(MESSAGE, message);
		return r;
	}
	
	public static R error(String message, int code) {
		R r = new R();
		r.put(CODE, 500);
		r.put(MESSAGE, message);
		return r;
	}
	
	public R result(Object result) {
		super.put(RESULT, result);
		return this;
	}
	
	public static R result(Object result, String message) {
		R r = new R();
		r.put(CODE, 200);
		r.put(RESULT, result);
		r.put(MESSAGE, message);
		return r;
	}
}
