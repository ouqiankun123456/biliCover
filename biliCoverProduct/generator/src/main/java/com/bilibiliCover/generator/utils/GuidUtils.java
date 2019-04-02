package com.bilibiliCover.generator.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月7日
 * @package com.bilibiliCover.generator.utils
 */
public class GuidUtils {
	
	public static String getGUID(int length) {
		String xString = String.valueOf(System.currentTimeMillis() + String.valueOf(Math.random()));
		char[] CHARS = (new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + getMD5(xString, false, 32)).toCharArray();
		char[] guid = new char[36];
		
		guid[8] = guid[13] = guid[18] = guid[23] = '-';
		guid[14] = '4';
		
		int j = 0;
		for(int i = 0; i< 36 ; i++) {
			if(guid[i] == 0) {
				guid[i] = CHARS[j];
				j++;
			}
		}
		if(length == 32) {
			return new String(guid).replace("-", "");
		}
		return new String(guid);
	}
	
	public static String getMD5(String pwd, boolean isUpper, Integer bit) {
		String md5 = new String();
		try {
			// 创建加密对象
			MessageDigest md = MessageDigest.getInstance("md5");
			if (bit == 64) {
				BASE64Encoder bw = new BASE64Encoder();
				String bsB64 = bw.encode(md.digest(pwd.getBytes("utf-8")));
				md5 = bsB64;
			} else {
				// 计算MD5函数
				md.update(pwd.getBytes());
				byte b[] = md.digest();
				int i;
				StringBuffer sb = new StringBuffer("");
				for (int offset = 0; offset < b.length; offset++) {
					i = b[offset];
					if (i < 0)
						i += 256;
					if (i < 16)
						sb.append("0");
					sb.append(Integer.toHexString(i));
				}
				md5 = sb.toString();
				if(bit == 16) {
					//截取32位md5为16位
					String md16 = md5.substring(8, 24).toString();
					md5 = md16;
					if (isUpper)
						md5 = md5.toUpperCase();
					return md5;
				}
			}
			//转换成大写
			if (isUpper)
				md5 = md5.toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("md5加密抛出异常！");
		}
 
		return md5;
	}
}
