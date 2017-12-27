package com.o2o.Util.util.ftp;

import com.deppon.cubc.web.common.constant.SFTPConstants;
import com.deppon.cubc.web.common.util.MD5Util;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * 创建SSH通道，封装工具类
 *
 * @author     tangyuhan 349910
 * @version    v1.0
 * @date       2016/12/24 15:58 下午
 */
public class JSchUtil {

    private Session session = null;
    private JSch jsch = null;
    private Channel channel = null;

    private static final Logger LOG = Logger.getLogger(JSchUtil.class);

    public JSchUtil() throws JSchException {
        connect();
    }

    public void connect() throws JSchException {
        jsch = new JSch(); // 创建JSch对象
        // 根据用户名，主机ip，端口获取一个Session对象
        session = jsch.getSession(MD5Util.convertMD5(SFTPConstants.SFTP_USERNAME), SFTPConstants.SFTP_HOST, SFTPConstants.SFTP_PORT);
        LOG.debug("Session created.");
        session.setPassword(MD5Util.convertMD5(SFTPConstants.SFTP_PASSWORD)); // 设置密码
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(SFTPConstants.SFTP_TIMEOUT); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        LOG.debug("Session connected.");
        channel = session.openChannel("sftp");
        channel.connect();
    }

    /**
     * 关闭连接
     */
    public void closeChannel() throws IOException {
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * 返回ChannelSftp,用于执行Linux操作
     */
    public ChannelSftp getChannelSftp() {
        return (ChannelSftp) channel;
    }

    /**
     *  将本地文件名为src的文件上传到目标服务器，
     *  @param src  本地路径
     *  @param dst  目标路径
     **/
    public String uploadFile(InputStream src, String dst) throws IOException {
        try {
            String[] folders = dst.substring(0,dst.lastIndexOf("/")).split("/");
            StringBuilder path = new StringBuilder(SFTPConstants.SFTP_DIR);
            for (String folder : folders) {
                if (folder.length() > 0) {
                    try {
                        path.append(File.separator).append(folder);
                        getChannelSftp().cd(path.toString());
                    } catch (SftpException e) {
                        getChannelSftp().mkdir(path.toString());
                        getChannelSftp().cd(path.toString());
                    }
                }
            }
            getChannelSftp().put(src, SFTPConstants.SFTP_DIR+File.separator+dst, new MyProgressMonitor(), ChannelSftp.OVERWRITE);
            getChannelSftp().quit();
            return SFTPConstants.SFTP_NETWORKDIR + dst;
        } catch (Exception e) {
            LOG.error(ExceptionUtils.getStackTrace(e));
        }finally{
            closeChannel();
            getChannelSftp().disconnect();
        }
        return null;
    }

    /**
     *  下载文件
     *  @param  fileName    下载文件
     *  @param  dst         目标路径
     **/
    public void downloadFile(String fileName,String dst) throws IOException {
        try {
            SftpATTRS attr = getChannelSftp().stat(fileName);
            long fileSize = attr.getSize();
            OutputStream out = new FileOutputStream(dst);
            getChannelSftp().get(fileName, out, new MyProgressMonitor(fileSize));
        } catch (Exception e) {
            LOG.error(ExceptionUtils.getStackTrace(e));
        }finally{
            if(null != channel)
                getChannelSftp().quit();
            closeChannel();
            getChannelSftp().disconnect();
        }
    }
}
