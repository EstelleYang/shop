package com.o2o.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CodeUtil{
        public static boolean checkVerifyCode(HttpServletRequest request) {
            String verifyCodeExpected = (String) request.getSession()
                    .getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
            String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
            if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
                return false;
            }
            return true;
        }
    public void save(String log) {
        String outFileName = "E:\\input1.txt";
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            // 输出文件
            File outFile = new File(outFileName);
            fw = new FileWriter(outFile, false);
            bw = new BufferedWriter(fw);
            bw.write(log);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.flush();
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
