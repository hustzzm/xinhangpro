package com.pig.basic.util;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;

public class ZipUtil {

    public static void zip(OutputStream out,File[] fileList){
        ZipArchiveOutputStream zous = new ZipArchiveOutputStream(out);
        try{
            for (File file : fileList) {
                String fileName = file.getName();
                InputStream inputStream = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                if (baos != null) {
                    baos.flush();
                }
                byte[] bytes = baos.toByteArray();

                //设置文件名
                ArchiveEntry entry = new ZipArchiveEntry(fileName);
                zous.putArchiveEntry(entry);
                zous.write(bytes);
                zous.closeArchiveEntry();
                if (baos != null) {
                    baos.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(zous!=null) {
                try {
                    zous.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
