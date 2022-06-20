package com.pig.basic.util.utils.poi;


import com.pig.basic.util.utils.ExceptionUtils;
import com.pig.basic.util.utils.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordUtils {

    private WordUtils() {
        ExceptionUtils.requireNonInstance();
    }

    public static final String BEGIN = "@";
    public static final String END = "@";
    private static final String regEx = BEGIN + "\\w+" + END;
    private static final Pattern pattern = Pattern.compile(regEx);
    public static final String TABLE = ".";
    private static final String regExTable = BEGIN + "\\w+\\" + TABLE + "\\w+" + END;
    private static final Pattern patternTable = Pattern.compile(regExTable);

    public static class Img {

        private final int width;
        private final int height;
        private final String path;
        private final String title;

        public Img(String path, String title, int width, int height) {
            this.width = width;
            this.height = height;
            this.path = path;
            this.title = title;
        }

        public Img(String path, String title) {
            this(path, title, 769, 153);
        }
    }

    public static class ImgList {
        private final List<Img> list = new ArrayList<>();

        public ImgList(List<Img> list) {
            this.list.addAll(list);
        }

        public List<Img> getList() {
            return new ArrayList<>(list);
        }
    }

    //处理\n
    public static void setRun(XWPFRun r, String content) {
        String[] nw = content.split("\n");
        for (int i = 0; i < nw.length; i++) {
            if (i == 0) {
                r.setText(nw[i], 0);//替换
            } else {
                r.setText(nw[i]);//追加
            }
            if (i != nw.length - 1) {
                r.addCarriageReturn();//回车
            }
        }
    }

    public static void setRun(XWPFRun r, String content, int a) {
        if (a == 0) {
            setRun(r, content);
        } else {
            String[] nw = content.split("\n");
            for (int i = 0; i < nw.length; i++) {
                r.setText(nw[i]);//追加
                if (i != nw.length - 1) {
                    r.addCarriageReturn();
                }
            }
        }
    }

    public static int getPictureType(String suffix) {
        if (StringUtils.isNotBlank(suffix)) {
            if (suffix.equalsIgnoreCase("emf")) {
                return Document.PICTURE_TYPE_EMF;//2
            }
            if (suffix.equalsIgnoreCase("wmf")) {
                return Document.PICTURE_TYPE_WMF;//3
            }
            if (suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg")) {
                return Document.PICTURE_TYPE_JPEG;//5
            }
            if (suffix.equalsIgnoreCase("png")) {
                return Document.PICTURE_TYPE_PNG;//6
            }
            if (suffix.equalsIgnoreCase("dib")) {
                return Document.PICTURE_TYPE_DIB;//7
            }
        }
        return Document.PICTURE_TYPE_PICT;//4
    }

    //处理图片
    public static void setRun(XWPFRun r, Img img, int a) {
        String suffix = StringUtils.getSuffix(img.path);
        try (InputStream is = Files.newInputStream(Paths.get(img.path))) {
            if (StringUtils.isNotBlank(img.title)) {
                setRun(r, img.title, a);
                r.addCarriageReturn();
            } else {
                setRun(r, "", a);
            }
            r.addPicture(is, getPictureType(suffix), "", Units.toEMU(img.width), Units.toEMU(img.height));
            r.addCarriageReturn();
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setRun(XWPFRun r, Img img) {
        setRun(r, img, 0);
    }

    public static void setRun(XWPFRun r, ImgList list) {
        List<Img> l = list.getList();
        for (int i = 0; i < l.size(); i++) {
            setRun(r, l.get(i), i);
        }
    }

    public static void setRun(XWPFRun r, Object o) {
        if (o instanceof String) {
            setRun(r, (String) o);
        } else if (o instanceof Img) {
            setRun(r, (Img) o);
        } else if (o instanceof ImgList) {
            setRun(r, (ImgList) o);
        } else {
            if (o == null) {
                setRun(r, "");
            } else {
                setRun(r, o.toString());
            }
        }
    }

    public static void handleRun(XWPFRun r, Map<String, Object> data) {
        String content = r.getText(r.getTextPosition());
        if (StringUtils.isNotBlank(content)) {
            if (pattern.matcher(content).matches()) {
                System.out.println("run:" + content);

                for (String k : data.keySet()) {
                    if (content.contains(BEGIN + k + END)) {
                        Object newCon = data.get(k);
                        setRun(r, newCon);
                    }
                }
            }

        }
    }

    public static void handleRuns(List<XWPFRun> rs, Map<String, Object> data) {
        for (XWPFRun r : rs) {
            handleRun(r, data);
        }
    }

    public static void handleParagraph(XWPFParagraph p, Map<String, Object> data) {
        handleRuns(p.getRuns(), data);
    }

    public static void handleParagraphs(List<XWPFParagraph> ps, Map<String, Object> data) {
        for (XWPFParagraph p : ps) {
            handleParagraph(p, data);
        }
    }

    public static void handleCell(XWPFTableCell c, Map<String, Object> data) {
        handleParagraphs(c.getParagraphs(), data);
    }

    public static void handleCells(List<XWPFTableCell> cs, Map<String, Object> data) {
        for (XWPFTableCell c : cs) {
            handleCell(c, data);
        }
    }

    public static void handleRow(XWPFTableRow r, Map<String, Object> data) {
        handleCells(r.getTableCells(), data);
    }

    public static void handleRows(List<XWPFTableRow> rs, Map<String, Object> data) {
        for (XWPFTableRow r : rs) {
            handleRow(r, data);
        }
    }

    public static void handleTable(XWPFTable t, Map<String, Object> data) {
        handleRows(t.getRows(), data);
    }

    public static void handleTables(List<XWPFTable> ts, Map<String, Object> data) {
        for (XWPFTable t : ts) {
            handleTable(t, data);
        }
    }

    public static void handleHeader(XWPFHeader header, Map<String, Object> data) {
        handleParagraphs(header.getParagraphs(), data);
    }

    public static void handleHeaders(List<XWPFHeader> headers, Map<String, Object> data) {
        for (XWPFHeader header : headers) {
            handleHeader(header, data);
        }
    }

    public static void clearCell(XWPFTableCell cell) {
        List<XWPFParagraph> l = cell.getParagraphs();
        for (int i = 0; i < l.size(); i++) {
            cell.removeParagraph(i);
        }
    }

    public static void setCell(XWPFTableCell cell, String content) {
        clearCell(cell);
        String[] nw = content.split("\n");
        for (String n : nw) {
            XWPFRun r = cell.addParagraph().createRun();
            r.setText(n);
        }
    }

    public static void setCell(XWPFTableCell cell, Object content) {
        if (content instanceof String) {
            setCell(cell, (String) content);
            return;
        }
        clearCell(cell);
        XWPFRun r = cell.addParagraph().createRun();
        setRun(r, content);
    }

    //核心代码
    public static boolean handleRowOfCell(XWPFTableRow row, Map<String, Object> data) {
        List<Map<String, Object>> l = new ArrayList<>();
        Map<Integer, String> keys = new HashMap<>();
        List<XWPFTableCell> cs = row.getTableCells();
        for (String k : data.keySet()) {
            for (int i = 0; i < cs.size(); i++) {
                XWPFTableCell c = cs.get(i);
                String text = c.getText();
                Matcher matcher = patternTable.matcher(text);
                if (matcher.matches()) {
                    if (text.contains(BEGIN + k + TABLE)) {
                        Object v = data.get(k);
                        if (v instanceof List) {
                            l = (List<Map<String, Object>>) v;
                            if (!l.isEmpty()) {
                                Map<String, Object> m = l.get(0);
                                for (String k2 : m.keySet()) {
                                    if (text.contains(TABLE + k2 + END)) {
                                        Object v2 = m.get(k2);
                                        if (StringUtils.isNotBlank(v2)) {
                                            keys.put(i, k2);
                                            setCell(c, text.replace(BEGIN + k + TABLE + k2 + END, v2.toString()));
                                        } else {
                                            setCell(c, "");
                                        }
                                    }
                                }
                            } else {
                                setCell(c, "");
                            }
                        }
                    }
                } else if (text.contains(BEGIN + k + END)) {
                    Object newCon = data.get(k);
                    setCell(c, newCon);
                    System.out.println("cell:" + text);
                }
            }
        }
        if (keys.isEmpty()) {
            return false;
        }
        int index = -1;
        XWPFTable table = row.getTable();
        List<XWPFTableRow> rs = table.getRows();
        for (int i = 0; i < rs.size(); i++) {
            XWPFTableRow r = rs.get(i);
            if (r == row) {
                index = i;
                break;
            }
        }
        for (int i = 1; i < l.size(); i++) {
            XWPFTableRow nr = table.insertNewTableRow(++index);
            nr.getCtRow().setTrPr(row.getCtRow().getTrPr());
            Map<String, Object> m = l.get(i);
            for (int j = 0; j < cs.size(); j++) {
                XWPFTableCell c = cs.get(j);
                XWPFTableCell nc = nr.addNewTableCell();
                nc.getCTTc().setTcPr(c.getCTTc().getTcPr());
                String k = keys.get(j);
                if (StringUtils.isNotBlank(k)) {
                    setCell(nc, m.get(k).toString());
                } else {
                    setCell(nc, "");
                }
            }
        }
        return true;
    }

    public static void handleTableOfCell(XWPFTable table, Map<String, Object> data) {
        for (XWPFTableRow row : table.getRows()) {
            if (handleRowOfCell(row, data)) {
                break;
            }
        }
    }

    public static void handleTablesOfCell(List<XWPFTable> tables, Map<String, Object> data) {
        for (XWPFTable table : tables) {
            handleTableOfCell(table, data);
        }
    }

    public static void handleHeaderOfCell(XWPFHeader header, Map<String, Object> data) {
        handleTablesOfCell(header.getTables(), data);
    }

    public static void handleHeadersOfCell(List<XWPFHeader> headers, Map<String, Object> data) {
        for (XWPFHeader header : headers) {
            handleHeaderOfCell(header, data);
        }
    }

    public static void templateCreateWord(String templatePath, String generatePath, Map<String, Object> data) {
        try (InputStream is = Files.newInputStream(Paths.get(templatePath))) {
            templateCreateWord(is, generatePath, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //为了使得模板的关键字不被拆分，建议使用表格
    public static void templateCreateWord(InputStream is, String generatePath, Map<String, Object> data) throws IOException {
        try (OutputStream os = Files.newOutputStream(Paths.get(generatePath))) {
            XWPFDocument document = new XWPFDocument(is);
            //段落
            System.out.println("开始处理段落==========================================");
            List<XWPFParagraph> ps = document.getParagraphs();
            handleParagraphs(ps, data);
            System.out.println("开始处理表格==========================================");
            //表格
            List<XWPFTable> ts = document.getTables();
            handleTables(ts, data);
            handleTablesOfCell(ts, data);
            System.out.println("开始处理页眉==========================================");
            //页眉
            List<XWPFHeader> headers = document.getHeaderList();
            handleHeaders(headers, data);
            handleHeadersOfCell(headers, data);
            document.write(os);
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
