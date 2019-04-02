package com.bilibiliCover.generator.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bilibiliCover.generator.entity.CovertemplateparamsBean;
import com.bilibiliCover.generator.service.CoverService;
import com.bilibiliCover.generator.utils.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 封面生成及使用控制层（客户端）
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年1月31日
 * @package com.bilibiliCover.generator.controller
 */
@RestController
@Api("封面生成及使用控制层（客户端）")
@RequestMapping("/coverGenerator")
public class CoverGeneratorController {
	
	@Autowired
	private CoverService coverService;
	
	/**
	 * 生成封面
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月9日
	 * @return R
	 * @param data
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value="生成封面")
	@PostMapping
	@ApiImplicitParams({
        @ApiImplicitParam(name = "templateKid", value = "模板kid", required = true, dataType = "String"),
        @ApiImplicitParam(name = "data", value = "用户输入表单数据", required = true, dataType = "String"),
        @ApiImplicitParam(name = "printSize", value = "输出的尺寸类型，如bilibili封面则输入\"bilibili\"", required = true, dataType = "String"),
	})
	public R generateCover(@RequestBody String data) {
		JSONObject jo = JSON.parseObject(data);
		String templateKid = jo.getString("templateKid");
		String dataStr = jo.getString("data");
		String printSize = jo.getString("printSize");
		HashMap dataMap = JSON.parseObject(dataStr, HashMap.class);
		String path = coverService.generateCover(templateKid, dataMap, printSize);
		return R.success("生成成功").result(path);
	}
	
	/**
	 * 获取用户需要填写的表单信息
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月23日
	 * @return R
	 * @param kid
	 * @return
	 */
	@ApiOperation(value="获取用户需要填写的表单信息")
	@GetMapping("/{kid}")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "kid", value = "模板kid", required = true, dataType = "String"),
	})
	public R getGenerateForm(@PathVariable("kid")String kid) {
		List<CovertemplateparamsBean> result = coverService.getGenerateForm(kid);
		return R.success("").result(result);
	}
	

}
