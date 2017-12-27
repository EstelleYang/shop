package com.o2o.Util.util.ftp;

import com.jcraft.jsch.SftpProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监控文件执行进度
 *
 * @author     tangyuhan 349910
 * @version    v1.0
 * @date       2016/12/24 17:27 下午
 */
public class MyProgressMonitor implements SftpProgressMonitor {

    private final static Logger logger = LoggerFactory.getLogger(MyProgressMonitor.class);

    private long transfered;

    public MyProgressMonitor() {}

    public MyProgressMonitor(long transfered) {
        this.transfered = transfered;
    }

    @Override
    public boolean count(long count) {
        transfered = transfered + count;
        logger.debug("当前传输的总大小: {} bytes", transfered);
        return true;
    }

    @Override
    public void end() {
        logger.debug("文件执行成功");
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        logger.debug("文件路径 >>>>>>>>> {}",src);
        logger.debug("目标路径 >>>>>>>>> {}",dest);
        logger.debug("文件大小 >>>>>>>>> {} bytes",max);
    }
}
