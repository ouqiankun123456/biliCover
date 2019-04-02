package com.bilibiliCover.generator.command;

import com.bilibiliCover.generator.utils.ImageUtils;

/**
 * 基础执行者
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月29日
 * @package com.bilibiliCover.generator.command
 */
public interface BaseReceiver {
	

	/**
	 * 执行者统一行动接口，具体调用ImageUtils操作方法
	 * {@link ImageUtils}
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年1月29日
	 * @return void
	 */
	 void doDraw();
}
