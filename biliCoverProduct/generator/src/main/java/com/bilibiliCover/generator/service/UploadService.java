package com.bilibiliCover.generator.service;

import java.io.IOException;
import java.util.List;

import com.bilibiliCover.generator.entity.MultipartFileParam;
import com.bilibiliCover.generator.entity.UploadFileBean;

/**
 * 上传服务层
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月10日
 * @package com.bilibiliCover.generator.service
 */
public interface UploadService {

	/**
     * 分块上传
     * 第一步：获取RandomAccessFile,随机访问文件类的对象f
     * 第二步：调用RandomAccessFile的getChannel()方法，打开文件通道 FileChannel
     * 第三步：获取当前是第几个分块，计算文件的最后偏移量
     * 第四步：获取当前文件分块的字节数组，用于获取文件字节长度
     * 第五步：使用文件通道FileChannel类的 map（）方法创建直接字节缓冲器  MappedByteBuffer
     * 第六步：将分块的字节数组放入到当前位置的缓冲区内  mappedByteBuffer.put(byte[] b);
     * 第七步：释放缓冲区
     * 第八步：检查文件是否全部完成上传
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月10日
	 * @return String
	 * @param param
	 * @return
	 * @throws IOException 
	 */
	String chunkUploadByMappedByteBuffer(MultipartFileParam param) throws IOException;

	/**
	 * 根据kid获取上传文件夹中对应图片的Bean
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年2月24日
	 * @return UploadFileBean
	 * @param name
	 * @return
	 */
	UploadFileBean selectByKid(String name);

	/**
	 * 根据delKids批量删除uploadFile表中的记录和对应的文件，节省服务器空间
	 * @author kayden
	 * @version 0.0.1
	 * @createTime 2019年3月21日
	 * @return void
	 * @param kids
	 */
	void deleteUploadFileResource(List<String> kids);

}
