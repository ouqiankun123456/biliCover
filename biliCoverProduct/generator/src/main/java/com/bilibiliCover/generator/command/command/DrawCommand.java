package com.bilibiliCover.generator.command.command;

import java.util.Optional;

import com.bilibiliCover.generator.command.BaseCommand;
import com.bilibiliCover.generator.command.BaseReceiver;

/**
 * 图画命令执行
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月29日
 * @package com.bilibiliCover.generator.command.command
 */
public class DrawCommand implements BaseCommand {

	private BaseReceiver baseReceiver = null;
	
	public DrawCommand(BaseReceiver b) {
		this.baseReceiver = b;
	}	
	
	@Override
	public void Execute() {
		Optional.of(baseReceiver).ifPresent(e -> e.doDraw());
	}

}
