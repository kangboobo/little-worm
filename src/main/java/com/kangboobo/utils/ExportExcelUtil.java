package com.kangboobo.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 导出文件工具类
 *
 * Created by kangboobo on 2019/08/15.
 */
@Service
public class ExportExcelUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 导出本地文件
     * @param response
     * @param file
     * @return
     */
    public HttpServletResponse exportExcelFromLocal(HttpServletRequest request, HttpServletResponse response, File file) {
        // 以流的形式下载文件。
        InputStream fis = null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        try {
            fis =  new FileInputStream(file);
            byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = fis.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
            // 清空response
            response.reset();
            // 设置response的Header
            // 传过来的文件名有可能包含时间戳，时间戳的标记从@到后缀的.之间，所以截取这部分内容不返回给前端
            String fileName = file.getName();
            if (StringUtils.isNotEmpty(fileName) && fileName.indexOf("@") > 0){
                fileName =  fileName.substring(0,fileName.indexOf("@"))+ fileName.substring(fileName.indexOf("."));
            }
            fileName = this.encodeChineseDownloadFileName(request,fileName);
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(in_b);
            toClient.flush();
            toClient.close();
        } catch (RuntimeException e) {
            logger.info("exportExcelFromLocal出现问题 RuntimeException", e);
            throw e;
        } catch (FileNotFoundException e) {
            logger.info("exportExcelFromLocal出现问题 FileNotFoundException", e);
        } catch (IOException e) {
            logger.info("exportExcelFromLocal出现问题 IOException", e);
        } catch (Exception e) {
            logger.info("exportExcelFromLocal出现问题 Exception", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (swapStream != null) {
                    swapStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return response;
    }


    private String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName) throws Exception {
        String filename = null;
        String agent = request.getHeader("USER-AGENT");
        if (null != agent){
            if (-1 != agent.indexOf("Firefox")) {//Firefox
                filename = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))+ "?=";
            }else if (-1 != agent.indexOf("Chrome") && agent.indexOf("Edge") < 0) {//Chrome
                filename = new String(pFileName.getBytes(), "ISO8859-1");
            } else {//IE7+
                filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
                filename = StringUtils.replace(filename, "+", "%20");//替换空格
            }
        } else {
            filename = pFileName;
        }
        return filename;
    }

    /**
     * copy文件到多台服务器
     * @param fileName
     *
     */
    public boolean callShellCopyFile(String fileName) {
        logger.info("callShellCopyFile start fileName:"+fileName);
        String shpath="/opt/shell/export.sh  " +fileName;
        String result = "1";
        try{
            Process ps = Runtime.getRuntime().exec(shpath);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
            logger.info("callShellCopyFile end result:"+result);
        } catch (Exception e) {
            logger.error("callShellCopyFile出错:",e);
        }
        return result.equals("0")?false:true;
    }

    /**
     * 删除多台服务器文件
     * @param fileName
     *
     */
    public boolean callShellDeleteFile(String fileName) {
        logger.info("callShellDeleteFile start fileName:"+fileName);
        String shpath="/opt/shell/delete.sh " +fileName;
        String result = "1";
        try{
            Process ps = Runtime.getRuntime().exec(shpath);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
            logger.info("callShellDeleteFile end result:"+result);
        } catch (Exception e) {
            logger.error("callShellDeleteFile出错:",e);
        }
        return result.equals("0")?false:true;
    }
}
