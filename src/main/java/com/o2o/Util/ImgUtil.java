package com.o2o.Util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * thumbnail是用户上传的图片文件流
 * targetAddr 图片存储路径
 * 封装thumbnailator的方法
 */
public class ImgUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImgUtil.class);
    public static File transferCommonsMutilpartFileToFile(CommonsMultipartFile commonsMultipartFile){
        File newFile = new File(commonsMultipartFile.getOriginalFilename());
        try{
            commonsMultipartFile.transferTo(newFile);

        }catch (IllegalStateException e){
            logger.error(e.toString());
            e.printStackTrace();
        }catch (IOException e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }
    /**
     * 处理缩略图，并返回新生成图片的相对路径
     * @param thumbnailStream
     * @param targerAddr
     * @return
     */
    public static String generateThumbnail(InputStream thumbnailStream,String filename,String targerAddr){
        //生成随机文件名
        String realFileName = getRandomFileName();
        //生成文件扩展名
        String extension = getFileExtension(filename);
        makeDirPath(targerAddr);
        String relativeAddr = targerAddr+realFileName+extension;
        logger.debug("current relativeAddr is:"+relativeAddr);
        File dest = new File(PathUtil.getImgBasePath()+relativeAddr);
        logger.debug("current complete addr is:"+PathUtil.getImgBasePath()+relativeAddr);
        try{
            Thumbnails.of(thumbnailStream)
                    .size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "/watermark.jpg")),0.25f)
                    .outputQuality(0.8f)
                    .toFile(dest);

        }catch (IOException e){
            e.printStackTrace();
            System.out.println(basePath+"/watermark.jpg");
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work/xxx.jpg,则home work 这两个文件夹都要自动创建
     * @param targerAddr
     */
    private static void makeDirPath(String targerAddr) {
        String realFileParentPath = PathUtil.getImgBasePath()+targerAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 获取用户上传的图片文件名的扩展名
     * @param cFile
     * @return
     */
    private static String getFileExtension(String cFile) {
        //获取扩展名
        return cFile.substring(cFile.lastIndexOf("."));
    }

    /**
     * 随机文件名的创建：当前时间+五位随机数
     * @return
     */
    public static String getRandomFileName(){
        //生成随机的五位数
        int rannum = r.nextInt(89999)+10000;
        //获取被格式化的当前时间
        String nowTime = sdf.format(new Date());
        return nowTime+rannum;
    }

    public static void deletePathOrfile(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
        if (fileOrPath.exists()){
            if (fileOrPath.isDirectory()){
                File list[] = fileOrPath.listFiles();
                for (int i=0;i<list.length;i++){
                    list[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

    public static void main(String[]args) throws IOException {
        //改变xiaohuangren.jpg的大小并打上水印，并对之后的图片进行压缩
        //获取classpath的绝对路径
        String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(basePath);
        File file = new File("E:\\code\\blog");
        System.out.println(file.getName());

//        Thumbnails.of(new File(basePath+"xiaohuangren.jpg"))
//                .size(200,200)
//                //水印的位置，读入水印图片的路径,透明度
//                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
//               //压缩图片，压缩比是80%
//                .outputQuality(0.8f)
//                .toFile("E:/xiaohuangrennews.jpg");    }
    }
}
