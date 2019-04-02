package com.bilibiliCover.generator.config;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bilibiliCover.generator.entity.CoverTemplateBean;
import com.bilibiliCover.generator.entity.UploadFileBean;
import com.bilibiliCover.generator.mapper.CoverTemplateMapper;
import com.bilibiliCover.generator.mapper.UploadFileMapper;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 定时任务
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月12日
 * @package com.bilibiliCover.generator.config
 */
@Slf4j
@Component
public class ScheduledService {
	
	@Value("${temporaryPath}")
	private String TEMPLATEPATH;
	
	@Value("${previewCoverPath}")
	private String PREVIEW_COVER_PATH;
	
	@Autowired
	private UploadFileMapper uploadFileMapper;
	
	@Autowired
	private CoverTemplateMapper coverTemplateMapper;
	
	/**
	 * 每一小时进行清空临时文件夹
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月12日
	 * @return void
	 */
	@Scheduled(cron = "0 0 0/1 * * ?")
    public void scheduled(){
		delAllFile(TEMPLATEPATH);
        log.info("=====>>>>>定时任务清空临时文件夹{}  {}",System.currentTimeMillis(), TEMPLATEPATH);
    }
	
	/**
	 * 每晚12点30分删除预览图文件夹中已被废弃的图片（即现有模板的previewImage字段外的其他图片）
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月20日
	 * @return void
	 */
	@Scheduled(cron = "0 30 0 * * ?")
	public void scheduledDeletePreviewCover() {
		log.info("=====>>>>>开始定时任务：清空预览图文件夹中已被废弃的图片{}  {}",System.currentTimeMillis(), PREVIEW_COVER_PATH);
		File file = new File(PREVIEW_COVER_PATH);
		if(!file.exists())
			return;
		else {
			// 查出所有在用模板
			List<CoverTemplateBean> list = coverTemplateMapper.select(new CoverTemplateBean().setIsdel(false));
			// 过滤出previewImage成List<String>
			List<String> previewImageKids = list.parallelStream()
					.filter(e -> StringUtils.isNotBlank(e.getPreviewImage()))
					.map(CoverTemplateBean::getPreviewImage).collect(Collectors.toList());
			File[] files = file.listFiles();
			for(File item : files) {
				String uploadFileKid = item.getName().substring(0, item.getName().lastIndexOf("."));
				// 判断此kid是否在previewImageKids中，如果不是则删除此文件
				if(!previewImageKids.contains(uploadFileKid)) {
					item.delete();
					Example example = new Example(UploadFileBean.class);
					example.createCriteria().andEqualTo("kid", uploadFileKid);
					uploadFileMapper.updateByExampleSelective(new UploadFileBean().setIsdel(true).setDelTime(Timestamp.valueOf(LocalDateTime.now()))
							, example);
				}
			}
		}
		log.info("=====>>>>>结束定时任务：清空预览图文件夹中已被废弃的图片{}  {}",System.currentTimeMillis(), PREVIEW_COVER_PATH);
	}
	
	//删除指定文件夹下所有文件
	//param path 文件夹完整绝对路径
   private boolean delAllFile(String path) {
       boolean flag = false;
       File file = new File(path);
       if (!file.exists()) {
         return flag;
       }
       if (!file.isDirectory()) {
         return flag;
       }
       String[] tempList = file.list();
       File temp = null;
       for (int i = 0; i < tempList.length; i++) {
          if (path.endsWith(File.separator)) {
             temp = new File(path + tempList[i]);
          } else {
              temp = new File(path + File.separator + tempList[i]);
          }
          if (temp.isFile()) {
             temp.delete();
          }
          if (temp.isDirectory()) {
             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
             delFolder(path + "/" + tempList[i]);//再删除空文件夹
             flag = true;
          }
       }
       return flag;
     }
	   
	 //删除文件夹
	 //param folderPath 文件夹完整绝对路径
      private void delFolder(String folderPath) {
	      try {
	         delAllFile(folderPath); //删除完里面所有内容
	         String filePath = folderPath;
	         filePath = filePath.toString();
	         java.io.File myFilePath = new java.io.File(filePath);
	         myFilePath.delete(); //删除空文件夹
	      } catch (Exception e) {
	        e.printStackTrace(); 
	      }
	 }
}
