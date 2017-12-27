package com.o2o.Util.util;

import com.deppon.cubc.commons.dao.Result;
import com.deppon.cubc.commons.dao.ResultList;
import com.deppon.cubc.commons.util.SpringBeanUtils;
import com.deppon.cubc.web.common.constant.NumberConstants;
import com.deppon.cubc.web.common.constant.SFTPConstants;
import com.deppon.cubc.web.common.util.ftp.JSchUtil;
import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * 文件工具类
 *
 * @author     tangyuhan 349910
 * @version    v1.0
 * @date       2016/12/22 22:08 下午
 */
public class FileUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    //文件服务器根路径
    private static String fileDir = (String)SpringBeanUtils.getBean("file_dir");

    private final static String RootPath = StringUtils.join(File.separator , "cubc");

    private final static String MODALT = "answer";

    //    /**
//     * 写入文件
//     * 储存规则  用户唯一标识 >> 模块名称 >> 年月份 >> 文件
//     *
//     * @param multipartFile 文件流
//     * @param userId        用户唯一标识
//     * @param modular       模块名称
//     * @return 物理路径
//     */
//    public static Result<File> writeFile(MultipartFile multipartFile, String userId, String modular) {
//        Result<File> result = new Result<File>();
//        logger.debug("导入文件名:{}", multipartFile.getOriginalFilename());
//        //获取文件名
//        String fileName = multipartFile.getOriginalFilename();
//        //拼接文件路径
//        StringBuilder filepath = new StringBuilder(userId);
//        filepath.append(File.separator).
//                append(modular).append(File.separator).append(DateUtil.formatDate(new Date(), "yyyyMM")).
//                append(File.separator).append(fileName);
//        logger.debug("导入文件路径:{}", filepath.toString());
//        JSchUtil jSchUtil = null;
//        try {
//            jSchUtil = new JSchUtil();
//            String netWork = jSchUtil.uploadFile(multipartFile.getInputStream(), filepath.toString());
//            result.setData(new File(netWork));
//            result.setSuccess(Boolean.TRUE);
//        } catch (Exception e) {
//            logger.debug("上传文件失败:{}", ExceptionUtils.getStackTrace(e));
//            result.setSuccess(Boolean.FALSE);
//        } finally {
//            try {
//                if (null != jSchUtil) {
//                    jSchUtil.closeChannel();
//                }
//            } catch (Exception e) {
//                logger.debug("连接关闭失败:{}", ExceptionUtils.getStackTrace(e));
//            }
//        }
//        return result;
//    }

    /**
     * 写入文件(字符转义)
     *
     * @param multipartFile
     * @param userId
     * @param modular
     * @return
     */
    public static Result<String> writeFileResultString(MultipartFile multipartFile, String userId, String modular) {
        Result<String> result = new Result<String>();
        logger.debug("导入文件名:{}", multipartFile.getOriginalFilename());
        //获取文件名
        String fileName = multipartFile.getOriginalFilename();
        //拼接文件路径
        StringBuilder filepath = new StringBuilder(userId);
        filepath.append(File.separator).
                append(modular).append(File.separator).append(DateUtil.formatDate(new Date(), "yyyyMM")).
                append(File.separator).append(fileName);
        logger.debug("导入文件路径:{}", filepath.toString());
        JSchUtil jSchUtil = null;
        try {
            jSchUtil = new JSchUtil();
            String netWork = jSchUtil.uploadFile(multipartFile.getInputStream(), filepath.toString());
            result.setData(netWork);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            logger.debug("上传文件失败:{}", ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
        } finally {
            try {
                if (null != jSchUtil) {
                    jSchUtil.closeChannel();
                }
            } catch (Exception e) {
                logger.debug("连接关闭失败:{}", ExceptionUtils.getStackTrace(e));
            }
        }
        return result;
    }


    /**
     * 写入文件
     * 储存规则  用户唯一标识 >> 模块名称 >>  年月份 >> 文件
     *
     * @param inputStream 文件流
     * @param userId      用户唯一标识
     * @param modular     模块名称
     * @return 物理路径
     */
    public static Result<String> writeFile(InputStream inputStream, String fileName, String userId, String modular) {
        Result<String> result = new Result<String>();
        logger.debug("导入文件名:{}", fileName);
        //拼接文件路径
        StringBuilder filepath = new StringBuilder(userId);
        filepath.append(File.separator).
                append(modular).append(File.separator).append(DateUtil.formatDate(new Date(), "yyyyMM")).
                append(File.separator).append(fileName).append(".xls");
        logger.debug("导入文件路径:{}", filepath.toString());
        JSchUtil jSchUtil = null;
        try {
            jSchUtil = new JSchUtil();
            String netWork = jSchUtil.uploadFile(inputStream, filepath.toString());
            result.setData(netWork);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            logger.debug("上传文件失败:{}", ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
        } finally {
            try {
                if (null != jSchUtil) {
                    jSchUtil.closeChannel();
                }
            } catch (Exception e) {
                logger.debug("连接关闭失败:{}", ExceptionUtils.getStackTrace(e));
            }
        }
        return result;
    }

    /**
     * 删除文件夹
     *
     * @param files 存放数据库路径
     * @return
     */
    public static Result<Void> removeFile(String[] files) {
        Result<Void> result = new Result<Void>();
        if (null == files || files.length < 1) {
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        JSchUtil jSchUtil = null;
        try {
            jSchUtil = new JSchUtil();
            ChannelSftp linux = jSchUtil.getChannelSftp();
            for (String file : files) {
                file = SFTPConstants.SFTP_DIR + file.substring(file.indexOf("userfiles") + NumberConstants.NUMBER_9, file.length());
                linux.rm(file);
                logger.debug("{} 文件删除成功", (file.substring(file.lastIndexOf("/") + 1)));
            }
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            logger.debug("文件删除失败:{}", ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
        } finally {
            try {
                if (null != jSchUtil) {
                    jSchUtil.closeChannel();
                }
            } catch (Exception e) {
                logger.debug("连接关闭失败:{}", ExceptionUtils.getStackTrace(e));
            }
        }
        return result;
    }

    
    
    
    
    /**
     * 写入文件
     * 储存规则  模块名称 >> 年月份 >> 文件
     *
     * @param multipartFile 文件流
     * @param modular       模块名称
     * @return 物理路径
     */
    public static Result<String> writeFile(MultipartFile multipartFile, String modular) {

        Result<String> result = new Result<String>();
        //获取文件名
        String fileName = multipartFile.getOriginalFilename();
        //拼接文件路径
        StringBuilder filepath = new StringBuilder(fileDir)
                .append(RootPath)
                .append(modular)
                .append(File.separator);
        //获取年月
        String date = DateUtil.formatDate(new Date(), "yyyyMM");
        filepath.append(date).append(File.separator);

        File folder = new File(filepath.toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            File file = new File(folder, fileName);
            multipartFile.transferTo(file);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result.setErrorMessage(ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        result.setData(StringUtils.join(fileDir, filepath.toString(), fileName));
        result.setSuccess(true);
        return result;
    }


    /**
     * 写入文件
     * 储存规则  模块名称 >> 年月份 >> 文件
     *
     * @param multipartFile 文件流
     * @param modular       模块名称
     * @return 物理路径
     */
    public static Result<String> writeFile(MultipartFile multipartFile, HttpServletRequest request) {

        Result<String> result = new Result<String>();
        //获取文件名
        String fileName = multipartFile.getOriginalFilename();

        //拼接文件路径
        StringBuilder filepath = new StringBuilder(fileDir)
                .append(File.separator)
                .append(MODALT)
                .append(File.separator);
        //获取年月
        String date = DateUtil.formatDate(new Date(), "yyyyMM");
        filepath.append(date).append(File.separator);

        File folder = new File(filepath.toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file;
        try {
            file = new File(folder, fileName);
            multipartFile.transferTo(file);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result.setErrorMessage(ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        result.setData(file.getAbsolutePath());
        result.setSuccess(true);
        return result;
    }

    public static Result<String> writeFile(MultipartFile multipartFile, String mou, HttpServletRequest request) {
        String[] str = {"jpg","png","gif","bmp","html","htm","zip","rar","xls","xlsx","doc","docx","pptx","pdf","ppt","sql","txt"};
        Result<String> result = new Result<String>();
        //获取文件名
        String fileName = multipartFile.getOriginalFilename();

        //拼接文件路径
        StringBuilder filepath = new StringBuilder(fileDir)
                .append(File.separator)
                .append(mou)
                .append(File.separator);
        //获取年月
        String date = DateUtil.formatDate(new Date(), "yyyyMM");
        filepath.append(date).append(File.separator);

        File folder = new File(filepath.toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file;
        try {
            file = new File(folder, fileName);
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if(!Arrays.asList(str).contains(suffix)){
                result.setErrorMessage("非法文件。");
                result.setSuccess(Boolean.FALSE);
                return result;
            }
            if(file.isFile()){
                result.setErrorMessage("文件已存在，请换个名字。");
                result.setSuccess(Boolean.FALSE);
                return result;
            }
            multipartFile.transferTo(file);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result.setErrorMessage(ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        result.setData(file.getAbsolutePath());
        result.setSuccess(true);
        return result;
    }
    /**
     * 删除文件
     * @param filePath       物理路径
     * @return
     */
    public static Result<File> deleteFile(String filePath) {
        Result<File> result = new Result<File>();
        try {
            File file = new File(filePath);
            if(file.exists()){
                file.deleteOnExit();
                result.setSuccess(Boolean.TRUE);
            }
        } catch (Exception e) {
            result.setSuccess(Boolean.FALSE);
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    /**
     * 读取文件集合
     *
     * @param type
     * @param userId
     * @param modular
     * @param date
     * @return
     */
    public static ResultList<File> readFiles(String type, String userId, String modular, String date) {
        ResultList<File> result = new ResultList<File>();
        try {
            if (StringUtils.isBlank(type) ||
                    StringUtils.isBlank(userId) ||
                    StringUtils.isBlank(modular)) {
                result.setSuccess(Boolean.FALSE);
                result.setErrorMessage("文件类型、用户唯一标识、模块名称不能为空");
                return result;
            }
            StringBuilder filePath = new StringBuilder(userId);
            filePath.append(File.separator).append(modular).append(File.separator).
                    append(type).append(File.separator).append(date).append(File.separator);
            File file = new File(fileDir + filePath.toString());
            File[] directorys = file.listFiles();
            if (directorys == null || directorys.length < 1) {
                result.setSuccess(Boolean.FALSE);
                result.setErrorMessage("该文件夹为空");
                return result;
            }
            List<File> files = new ArrayList<File>();
            for (File f : directorys) {
                if (f.isFile()) {
                    files.add(new File(StringUtils.join(fileDir, filePath.toString(), f.getName())));
                }
            }
            result.setDatalist(files);
            result.setTotal(files.size());
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    /**
     * 读取单个文件
     *
     * @param userId
     * @param type
     * @param fileName
     * @return
     */
    public static Result<File> readFile(String userId, String type, String fileName) {
        Result<File> result = new Result<File>();
        try {
            StringBuilder filePath = new StringBuilder(userId);
            File file = new File(StringUtils.join(fileDir,
                    File.separator, filePath.toString(), File.separator));
            File[] files = file.listFiles();
            if (files == null || files.length < 1) {
                return result;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    File f2 = new File(StringUtils.join(f.getPath(),
                            File.separator, type, File.separator));
                    result = checkFile(f2, fileName);
                    if (result.isSuccess()) {
                        String path = result.getData().getPath();
                        //物理路径转换物理路径
                        path = StringUtils.join(fileDir, path.substring(path.indexOf(userId)));
                        result.setData(new File(path));
                        break;
                    }
                }
            }
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    /**
     * 搜索文件是否存在
     *
     * @param file
     * @param fileName
     * @return
     */
    public static Result<File> checkFile(File file, String fileName) {
        Result<File> result = new Result<File>();
        try {
            if (file.exists()) {
                LinkedList<File> list = new LinkedList<File>();
                File[] files = file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                    }
                }
                File tempFile;
                while (!list.isEmpty()) {
                    tempFile = list.removeFirst();
                    files = tempFile.listFiles();
                    if (files == null || files.length < 1) {
                        result.setSuccess(Boolean.FALSE);
                        return result;
                    }
                    for (File file2 : files) {
                        if (file2.isFile()) {
                            if (file2.getName().equalsIgnoreCase(fileName)) {
                                result.setSuccess(Boolean.TRUE);
                                result.setData(file2);
                                return result;
                            }
                            list.add(file2);
                        }
                    }
                }
            }
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage("文件不存在");
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage(ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    public static void main(String[] args) {

    }
}


