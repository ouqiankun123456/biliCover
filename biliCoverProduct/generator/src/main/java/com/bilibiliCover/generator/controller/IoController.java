package com.bilibiliCover.generator.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bilibiliCover.generator.constant.FilePathEnum;
import com.bilibiliCover.generator.entity.MultipartFileParam;
import com.bilibiliCover.generator.entity.UploadFileBean;
import com.bilibiliCover.generator.service.UploadService;
import com.bilibiliCover.generator.utils.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import tk.mybatis.mapper.entity.Example;

/**
 * 输入输出控制层
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月7日
 * @package com.bilibiliCover.generator.controller
 */
@Log
@Api("输入输出控制层")
@RestController
@RequestMapping("/io")
public class IoController {
	
	@Value("${temporaryPath}")
	private String TEMPLATEPATH;
	
	@Value("${uploadFilePath}")
	private String UPLOADFILEPATH;
	
	@Value("${rootPath}")
	private String ROOTPATH; // 根目录
	
	@Autowired
	private UploadService uploadService;
	
	/**
	 * 下载文件
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月9日
	 * @return void
	 * @param req
	 * @param resp
	 * @param name 文件的名字（包括后缀名），在临时文件夹找到并打开流返回前台，用于下载生成的封面
	 * @param type 文件的类型 不同的类型用不同的路径 temp: 临时文件夹 uploadFile: 长久保存的文件
	 */
	@GetMapping("/download")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "文件的名字（包括后缀名），在对应文件夹找到并打开流返回前台，用于下载生成的封面", required = true, dataType = "String"),
        @ApiImplicitParam(name = "type", value = "文件的类型 不同的类型用不同的路径 temp: 临时文件夹 uploadFile: 长久保存的文件", required = true, dataType = "String"),
	})
    public void downloadFile(HttpServletRequest req, HttpServletResponse resp, @RequestParam("name") String name
    		, @RequestParam("type") String type) {
        String downloadName= "封面.png";
//        进行转码后的文件名，用来下载之后的文件名
            String fileName = null;
            try {
                fileName = new String(downloadName.getBytes("GBK"), "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String realPath;
            switch (FilePathEnum.tran(type)) {
			case TEMP:
				// 临时文件夹
				realPath = ROOTPATH + name;
				break;
			case UPLOADFILE:
				// 上传文件夹, 传入的是kid,在uploadFile表中找到savePath
				UploadFileBean ufb = uploadService.selectByKid(name);
				realPath = ufb.getSavePath();
				// 替换后缀
				fileName = "封面" + ufb.getExt();
//				realPath = UPLOADFILEPATH + File.separator + name;
				break;
			default:
				throw new RuntimeException("参数输入错误，无法定位文件夹");
			}       
//            String realPath=File.separator+"home"+File.separator+"tomcat"+File.separator+"apache-tomcat-9.0.1"+File.separator+"files";
//            String path = name;
            File file = new File(realPath);
            if(!file.exists())
            	switch (FilePathEnum.tran(type)) {
				case TEMP:
					throw new RuntimeException("文件不存在，请重新生成封面");
				case UPLOADFILE:
					throw new RuntimeException("文件不存在，请重新上传");
				default:
					throw new RuntimeException("文件不存在");
				}      	
            resp.reset();       
            String fileExt = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            resp.setContentType("image/" + fileExt);
//            resp.setCharacterEncoding("utf-8");
            resp.setContentLength((int) file.length());
            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = resp.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	
	@ApiOperation("大文件分片上传")
    @PostMapping("/chunkUpload")
    public R fileChunkUpload(MultipartFileParam param, HttpServletResponse response, HttpServletRequest request){
        /**
         * 判断前端Form表单格式是否支持文件上传
         */
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(!isMultipart){
            //这里是我向前端发送数据的代码，可理解为 return 数据; 具体的就不贴了
            throw new RuntimeException("不支持的表单格式");
        }
        // 获取后缀名
        
        String ext = StringUtils.lowerCase(param.getFile().getOriginalFilename().substring(param.getFile().getOriginalFilename().lastIndexOf(".") + 1));
//        String ext = "." + param.getFile().getContentType().substring(param.getFile().getContentType().lastIndexOf("////") + 1);
        param.setExt(ext);
        if(!StringUtils.endsWithAny(ext, new String[] {"jpg", "png", "jepg"}))
        	throw new RuntimeException("请上传jpg、png、jepg格式的文件");
        if(param.getFile().getSize() > 2048000)
        	throw new RuntimeException("请不要上传大于2M的文件");
        log.info("上传文件 start...");
        try {
            String taskId = uploadService.chunkUploadByMappedByteBuffer(param);
            return R.success("上传成功").result(taskId);
        } catch (IOException e) {
            log.log(null, "文件上传失败。{}", param.toString());
        }
        log.info("上传文件结束");
        return R.error("上传失败");
    }
	
	/**
	 * 批量删除uploadFile文件夹文件
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年3月21日
	 * @return R
	 * @param delKids
	 * @return
	 */
	@ApiOperation("根据delKids批量删除uploadFile表中的记录和对应的文件，节省服务器空间")
	@DeleteMapping()
	@ApiImplicitParams({
        @ApiImplicitParam(name = "delKids", value = "uploadFile的kid组成的批量串", required = true, dataType = "String"),
	})
	public R deleteUploadFileResource(@RequestParam("delKids")String delKids) {
		List<String> kids = Arrays.asList(delKids.split(","));
		if(kids.isEmpty())
			throw new RuntimeException("请输入至少一个kid");
		uploadService.deleteUploadFileResource(kids);
		return R.success("删除文件成功");
	}

}
