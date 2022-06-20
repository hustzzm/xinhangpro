package com.pig.modules.wes.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
@Slf4j
public class FileUtils {
    /**
     * 字符串写入文件
     * @param content
     * @param fileName
     */
    public static void writeStringToFile(String content, String fileName) {
        log.info("phenotype file:" + fileName);
        try(FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            log.error("phenotypeWriteException",e);
        }
    }

    /**
     * json文件转为JSONObject
     * @param file
     * @return
     * @throws IOException
     */
    public static JSONObject parseFileToJson(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        String jsonString = new String(buffer, StandardCharsets.UTF_8);
        JSONObject resultData = JSONObject.parseObject(jsonString);
        return resultData;
    }

    /**
     * json文件转JSONObject
     * @param filename
     * @return
     * @throws IOException
     */
    public static JSONObject parseFileToJson(String filename) throws IOException {
        File file = new File(filename);
        JSONObject object = parseFileToJson(file);
        return object;
    }
    /**
     * 查找文件是否存在
     * @param fileName
     * @return
     */
    public static boolean findFile(String fileName) {
        File file = new File(fileName);
        return file.isFile();
    }

    /**
     * 删除文件
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName){
        File file = new File(fileName);
        if(file.isFile()){
            return file.delete();
        }
        return true;
    }

    /**
     * 删除文件夹
     * @param filePath
     * @return
     */
    public static void deleteDirectory(String filePath){
        File file = new File(filePath);
        log.info("deleteFileDirectory:" + file.getAbsolutePath());
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFiles(files[i]);
            }
        }
    }

    /**
     * 递归删除文件/文件夹
     * @param file
     */
    public static void deleteFiles(File file) {
        if(file.exists()) {
            if(file.isDirectory()){
                Arrays.stream(file.listFiles()).forEach(f -> deleteFiles(f));
            }
            log.info("deleteFile:" + file.getAbsolutePath());
            file.delete();
        }
    }

}
