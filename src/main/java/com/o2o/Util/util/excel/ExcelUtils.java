package com.o2o.Util.util.excel;

import com.esafenet.dll.FileDlpUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: ExcelUtils
 * @Description: Excel工具类
 * @author 349910 tangyuhan
 * @date 2017/3/27 9:46
 *
 */
public class ExcelUtils {
    private final static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    public static String excelDecrypt(File file) throws IOException{
        //获取文件路径
        String path = file.getAbsolutePath();
        logger.error("旧的path"+path);
        //获取解密文件
        String unPath = FileDlpUtil.getDecryptFile(path);
        //删除原文件
        File decryptFile = new File(path);
        //删除加密文件
        decryptFile.delete();
        //新的文件
        return unPath;
    }

    public static String excelDecrypt(MultipartFile multipartFile) throws IOException{
        CommonsMultipartFile cf= (CommonsMultipartFile)multipartFile;
        DiskFileItem fi = (DiskFileItem)cf.getFileItem();
        File file = fi.getStoreLocation();
        String excelDecrypt = excelDecrypt(file);
        return excelDecrypt;
    }

    public static void main(String[] args) {
        try {
            File file = new File("d:\\349910\\Desktop\\rirfilt(正确正单).xls");
            ExcelUtils.excelDecrypt(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
