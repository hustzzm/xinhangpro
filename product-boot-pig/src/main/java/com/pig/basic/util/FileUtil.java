package com.pig.basic.util;

import com.pig.basic.constant.SystemConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 吴莹桂
 * 
 */
public class FileUtil {

    protected static String split_path_str = JudgeSystem.isLinux() ? SystemConstant.split_path_linux : SystemConstant.split_path_windows;
	// level用于记录目录的级数。
	public static List findFileList(String filedir) {

	    File dir = new File(filedir);
		File files[] = dir.listFiles();
		List list = new ArrayList();
		if (null == files) {
			return list;
		}

		for (File file : files) {

            if(file.getName().length() >= 6){
                String casepath = filedir + file.getName();
                File secdir = new File(casepath);

                File secfiles[] = secdir.listFiles();
                if(secfiles != null){
                    boolean bexistfinal = false;
                    for (File secfile : secfiles) {
                        if(secfile.getName().equals("final")){
                            bexistfinal = true;
                            break;
                        }
                    }
                    if(bexistfinal)
                        list.add(file.getAbsolutePath().toString());
                }
            }
		}
		return list;
	}

    // level用于记录目录的级数。
    public static String findTrioFile(String filedir,String endFilename) {
        String filename = "";
        File secdir = new File(filedir);

        File secfiles[] = secdir.listFiles();
        if(secfiles!=null){
            for (File secfile : secfiles) {
                if(secfile.getName().toLowerCase().startsWith("trio_") && secfile.getName().toLowerCase().endsWith(endFilename)){
                    filename = secfile.getPath();
                    break;
                }
            }
        }
        return filename;
    }

	/*
	根据文件路径全名获取文件名
	@filepath 文件路径
	 */
    public static String getFilename(String filepath) {
        String filename = "";
//        String regString = filepath.lastIndexOf("/") > 0 ? "/" : "\\";
        if (filepath.length() > 0) {
            filename = filepath.substring(filepath.lastIndexOf(split_path_str) + 1);
        }
        return filename;
    }

	// 根据目录级数产生空格。
	public static String getSpace(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append("  ");
		}
		return sb.toString();
	}
	
	/**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

}
