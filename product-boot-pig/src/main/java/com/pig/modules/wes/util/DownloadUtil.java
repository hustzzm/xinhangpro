package com.pig.modules.wes.util;

import org.apache.poi.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
*@Description   关于下载文件的通用方法
*@Author        wengzhongjie
*@Date          2022/2/23 13:23
*@Version
*/
public class DownloadUtil {

    /**
     * httpServletResponse下载文件到前端
     * @param response
     * @param filename
     * @param filePath
     */
    public static void downloadFile(HttpServletResponse response,String filename,String filePath){
        //通过Response把数据以Excel格式保存
        response.reset();
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        //输入流 根据自己业务形成的输入流
        try(OutputStream out = response.getOutputStream();FileInputStream is = new FileInputStream(filePath)){
            response.addHeader("Content-Disposition","attachment;"+ "filename="+ new String(filename.getBytes("GBK"),"ISO8859_1"));
            //Apache commons包下的IOUtils
            IOUtils.copy(is,out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
