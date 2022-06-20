package com.pig.basic.util;

import cn.hutool.poi.excel.ExcelWriter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author :  linqi
 * @date :  2021/12/27
 */
public class ExportUtil {

    private static final int FIRST_COLUNM = 0;
    private static final int FIRST_COLUNM_WIDTH = 20;
    private static final int MERGE_START = 1;
    private static final int MERGE_END = 8;

    /**
     * 自定义筛选页具体的excel模板，以键值为基础
     * @param writer
     * @param sheetName
     * @param keys
     * @param values
     * @return
     */
    public ExcelWriter createSheet(ExcelWriter writer, String sheetName, List<String> keys, List<String> values){
        writer.setSheet(sheetName);
        boolean emptyRight = values.size() == 0;
        //writer.setColumnWidth(FIRST_COLUNM,FIRST_COLUNM_WIDTH);
        for (int i = 0; i < keys.size(); i++) {
            writer.setColumnWidth(i,FIRST_COLUNM_WIDTH);
            int currentRow = writer.getCurrentRow();
            String key = keys.get(i);
            //values可能是空数组，防止越界
            String value = emptyRight ?  "" : values.get(i);
            //写第一列
            writer.writeCellValue(FIRST_COLUNM,currentRow, key);
            //合并后面的列
            writer.merge(currentRow, currentRow, MERGE_START, MERGE_END, value, false);
            writer.passCurrentRow();
            writer.passCurrentRow();
        }
        writer.getStyleSet().setWrapText();
        return writer;
    }

    /**
     * 表型更新/反查/自定义的Excel模板
     * @param writer
     * @param sheetName
     * @param firstSheet
     * @param header
     * @param header2
     * @param content
     * @return
     */
    public ExcelWriter createSheet(ExcelWriter writer, String sheetName, boolean firstSheet, Map<String, String> header, List<String> header2, List<List<String>> content){
        if(firstSheet){
            writer.renameSheet(sheetName);
        }else {
            writer.setSheet(sheetName);
        }
        Set<String> headKeys = header.keySet();
        for (String key: headKeys) {
            int currentRow = writer.getCurrentRow();
            writer.writeCellValue(FIRST_COLUNM,currentRow, key);
            writer.merge(currentRow,currentRow,MERGE_START,MERGE_END,header.get(key),false);
            writer.passCurrentRow();
            writer.passCurrentRow();
        }
        writer.writeRow(header2);
        for (int i = 0; i < content.size(); i++) {
            writer.setColumnWidth(i,20);
            writer.writeRow(content.get(i));
        }
        writer.getStyleSet().setWrapText();
        return writer;
    }
}
