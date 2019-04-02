package com.bilibiliCover.generator.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

import com.luciad.imageio.webp.WebPReadParam;
import com.luciad.imageio.webp.WebPWriteParam;

/**
 * webp转换工具类
 * @author kayden
 * @version 0.0.1
 * @createTime 2019年2月25日
 * @package com.bilibiliCover.generator.utils
 */
public class WebpUtils {

    public static String webp2png(String src, String dst) throws IOException {
        // Obtain a WebP ImageReader instance
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

        // Configure decoding parameters
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);

        // Configure the input on the ImageReader
        reader.setInput(new FileImageInputStream(new File(src)));

        // Decode the image
        BufferedImage image = reader.read(0, readParam);

        //the `png` can use `jpg`
        ImageIO.write(image, "png", new File(dst));
        return dst;
    }

    public static String png2webp(String src, String dst) throws IOException {
        // Obtain an image to encode from somewhere
        BufferedImage image = ImageIO.read(new File(src));

        // Obtain a WebP ImageWriter instance
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

        // Configure encoding parameters
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(WebPWriteParam.MODE_DEFAULT);

        // Configure the output on the ImageWriter
        writer.setOutput(new FileImageOutputStream(new File(dst)));

        // Encode
        writer.write(null, new IIOImage(image, null, null), writeParam);

        return dst;
    }
}
