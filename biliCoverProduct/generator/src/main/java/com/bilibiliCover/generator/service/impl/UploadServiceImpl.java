package com.bilibiliCover.generator.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bilibiliCover.generator.constant.FilePathEnum;
import com.bilibiliCover.generator.entity.MultipartFileParam;
import com.bilibiliCover.generator.entity.UploadFileBean;
import com.bilibiliCover.generator.mapper.UploadFileMapper;
import com.bilibiliCover.generator.service.UploadService;
import com.bilibiliCover.generator.utils.FileUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * 上传服务层实现类
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月10日
 * @package com.bilibiliCover.generator.service.impl
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {
	
	@Value("${uploadFilePath}")
	public String UPLOADFILEPATH;
	
	@Value("${temporaryPath}")
	private String TEMUPLOADFILEPATH;
	
	@Autowired
	private UploadFileMapper uploadFileMapper;

	@Override
	public String chunkUploadByMappedByteBuffer(MultipartFileParam param) throws IOException{
		if(param.getTaskId() == null || "".equals(param.getTaskId())){
            param.setTaskId(UUID.randomUUID().toString());
        }
        /**
         * basePath是我的路径，可以替换为你的
         * 1：原文件名改为UUID
         * 2：创建临时文件，和源文件一个路径
         * 3：如果文件路径不存在重新创建
         */
        String fileName = param.getFile().getOriginalFilename();
        //fileName.substring(fileName.lastIndexOf(".")) 这个地方可以直接写死 写成你的上传路径
        String tempFileName = param.getTaskId() + fileName.substring(fileName.lastIndexOf(".")) + "_tmp";
        String filePath = null;
        switch (FilePathEnum.tran(param.getUploadPath())) {
		case TEMP:
			filePath = TEMUPLOADFILEPATH; // 暂时先存入临时文件夹，等有登陆功能再记录			
			break;
		case UPLOADFILE:
			filePath = UPLOADFILEPATH;
			break;
		default:
			break;
		}
        File fileDir = new File(filePath);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File tempFile = new File(filePath,tempFileName);
        //第一步
        RandomAccessFile raf = new RandomAccessFile(tempFile,"rw");
        //第二步
        FileChannel fileChannel = raf.getChannel();
        //第三步
        long offset = param.getChunk() * param.getSize();
        //第四步
        byte[] fileData = param.getFile().getBytes();
        //第五步
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,offset,fileData.length);
        //第六步
        mappedByteBuffer.put(fileData);
        //第七步
        FileUtil.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        raf.close();
        //第八步
        boolean isComplete = checkUploadStatus(param,fileName,filePath);
        String result = "";
        if(isComplete){
        	String newName = param.getTaskId() + fileName.substring(fileName.lastIndexOf("."));
            renameFile(tempFile, newName);
            switch (FilePathEnum.tran(param.getUploadPath())) {
    		case TEMP:
    			result = TEMUPLOADFILEPATH.substring(TEMUPLOADFILEPATH.lastIndexOf(File.separator)) + File.separator + newName;
    			break;
    		case UPLOADFILE:
    			// 如果是存入uploadFile文件夹则记录进uploadFile表
                UploadFileBean ufb = new UploadFileBean().setKid(param.getTaskId())
                		.setExt(param.getExt()).setSavePath(tempFile.getParent()+ File.separatorChar + newName);
                uploadFileMapper.insertSelective(ufb);
                result = param.getTaskId();
                break;
    		default:
    			break;
    		}         
            return result;
        }
        return param.getTaskId();
    }

    /**
     * 文件重命名
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     * @return
     */
    private boolean renameFile(File toBeRenamed, String toFileNewName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            return false;
        }
        String p = toBeRenamed.getParent();
        File newFile = new File(p + File.separatorChar + toFileNewName);
        //修改文件名
        return toBeRenamed.renameTo(newFile);
    }

    /**
     * 检查文件上传进度
     * @return
     */
    private boolean checkUploadStatus(MultipartFileParam param,String fileName,String filePath) throws IOException {
        File confFile = new File(filePath,fileName+".conf");
        RandomAccessFile confAccessFile = new RandomAccessFile(confFile,"rw");
        //设置文件长度
        confAccessFile.setLength(param.getChunkTotal());
        //设置起始偏移量
        confAccessFile.setLength(param.getChunk());
        //将指定的一个字节写入文件中 127，
        confAccessFile.write(Byte.MAX_VALUE);
        byte[] completeStatusList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        //这一段逻辑有点复杂，看的时候思考了好久，创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认的0,已上传的就是Byte.MAX_VALUE 127
        for(int i = 0; i<completeStatusList.length && isComplete==Byte.MAX_VALUE; i++){
            isComplete = (byte)(isComplete & completeStatusList[i]);
            System.out.println("check part " + i + " complete?:" + completeStatusList[i]);
        }
        confAccessFile.close();
        if(isComplete == Byte.MAX_VALUE){
            return true;
        }
        return false;
    }

	@Override
	public UploadFileBean selectByKid(String name) {
		List<UploadFileBean> ufbs =  uploadFileMapper.select(new UploadFileBean().setKid(name).setIsdel(false));
		if(ufbs.isEmpty())
			throw new RuntimeException("没有找到对应的文件或文件已被删除");
		else
			return ufbs.get(0);
	}

	@Transactional
	@Override
	public void deleteUploadFileResource(List<String> kids) {
		// 查出有关记录，删除文件夹中的文件
		Example example = new Example(UploadFileBean.class);
		example.createCriteria().andIn("kid", kids);
		List<UploadFileBean> list = uploadFileMapper.selectByExample(example);
		if(list.isEmpty())
			throw new RuntimeException("没有相关的记录进行删除");
		list.stream().forEach(e -> {
			FileUtils.deleteQuietly(new File(e.getSavePath()));
		});
		// 逻辑删除数据库记录
		uploadFileMapper.deleteByExample(example);
	}

}
