package com.bilibiliCover.generator.command;

/**
 * 基础命令接口
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月29日
 * @package com.bilibiliCover.generator.command
 */
public interface BaseCommand {

	/**
	 * 执行命令，所有派生的命令类都需要实现此接口
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年1月29日
	 * @return void
	 */
	void Execute();
}
