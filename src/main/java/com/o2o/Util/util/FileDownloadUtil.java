package com.o2o.Util.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 文件下载工具类
 * @author 401681
 *
 */
public class FileDownloadUtil {
	//private static FileOutputStream fs;
    
    public static HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
          //  String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件。
           // String str =   "/opt/supplier/201703/20170306.png";
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
    
	/**
	 * FTP单个文件下载
	 * 
	 * @param ftpUrl
	 *            ftp地址
	 * @param userName
	 *            ftp的用户名
	 * @param password
	 *            ftp的密码
	 * @param directory
	 *            要下载的文件所在ftp的路径名不包含ftp地址
	 * @param destFileName
	 *            要下载的文件名
	 * @param downloadName
	 *            下载后锁存储的文件名全路径
	 */
	/*public static boolean download(String logSeq, String ftpUrl, String userName, int port, String password,
			String directory, String destFileName, String downloadName) throws IOException {
		FTPClient ftpClient = new FTPClient();
		boolean result = false;
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setBufferSize(1024);
			// 设置文件类型（二进制）
			ftpClient.changeWorkingDirectory(directory);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			System.out.println("destFileName:" + destFileName + ",downloadName:" + downloadName);
			result = ftpClient.retrieveFile(destFileName, new FileOutputStream(downloadName));
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}*/
  /*  public static void downloadLocal(HttpServletResponse response) throws FileNotFoundException {
        // 下载本地文件
        String fileName = "Operator.doc".toString(); // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream("c:/Operator.doc");// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void downloadNet(HttpServletResponse response,String httpBasePath) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        // path是指欲下载的文件的路径。
        File file = new File(httpBasePath);
        // 取得文件名。
        String filename = file.getName();
        URL url = new URL(httpBasePath);
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            fs = new FileOutputStream("c:/"+filename);
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}


