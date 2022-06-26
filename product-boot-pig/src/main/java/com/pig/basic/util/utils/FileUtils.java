package com.pig.basic.util.utils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.poi.xwpf.usermodel.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public abstract class FileUtils {

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        }
        file.delete();
    }

    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }

    public static void zipFile(String source, String target) {
        File file = new File(source);
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(target)))) {
            zipFile(zos, file, "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void zipFile(ZipOutputStream zos, File file, String base) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                zipFile(zos, f, StringUtils.concatPathIgnoreEmpty(base, file.getName()));
            }
        } else {
            zos.putNextEntry(new ZipEntry(StringUtils.concatPath(base, file.getName())));
            try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
                readFile(zos, is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void zipFile(File[] files, OutputStream os) {
        try (ZipOutputStream zos = new ZipOutputStream(os)) {
            for (File f : files) {
                ZipEntry zipEntry = new ZipEntry(f.getName());
                zos.putNextEntry(zipEntry);
                try (InputStream is = new BufferedInputStream(new FileInputStream(f))) {
                    readFile(zos, is);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static void readFile(OutputStream os, InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        int length;
        while ((length = is.read(bytes)) != -1) {
            os.write(bytes, 0, length);
        }
    }


    public static boolean createFile(String path, String content) {
        File file = new File(path);
        File f = file.getParentFile();
        if (!f.exists()) {
            f.mkdirs();
        }
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
            os.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createDirectory(String path) {
        return new File(path).mkdirs();
    }


    public static void unzipFile(ZipInputStream is, String target) throws IOException {
        ZipEntry zipEntry = is.getNextEntry();
        while (zipEntry != null) {
            if (zipEntry.isDirectory()) {
                createDirectory(StringUtils.concatPath(target, zipEntry.getName()));
            } else {
                String p = StringUtils.concatPath(target, zipEntry.getName());
                createDirectoryParent(p);
                try (OutputStream os = new BufferedOutputStream(new FileOutputStream(p))) {
                    readFile(os, is);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            is.closeEntry();
            zipEntry = is.getNextEntry();
        }
    }

    public static boolean createDirectoryParent(File file) {
        File f = file.getParentFile();
        return createDirectoryIs(f);
    }

    public static boolean createDirectoryParent(Path path) {
        return createDirectoryParent(path.toFile());
    }

    public static boolean createDirectoryParent(String path) {
        return createDirectoryParent(new File(path));
    }

    public static boolean createDirectoryIs(File file) {
        if (!file.exists()) {
            file.mkdirs();
            return true;
        }
        return false;
    }


    public static void createWordFtl(Map<?, ?> dataMap, String inPath, String outPath) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(outPath))) {
            Configuration configuration = new Configuration(new Version("2.3.30"));
            configuration.setDefaultEncoding("utf-8");
            File file = new File(inPath);
            configuration.setDirectoryForTemplateLoading(file.getParentFile());
            Template template = configuration.getTemplate(file.getName(), "utf-8");
            template.process(dataMap, writer);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createWord2Ftl(Map<?, ?> dataMap, String name, String outPath) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(outPath))) {
            Configuration configuration = new Configuration(new Version("2.3.30"));
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(FileUtils.class, "/templates/");
            Template template = configuration.getTemplate(name, "utf-8");
            template.process(dataMap, writer);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getByte(String path) {
        try (InputStream is = Files.newInputStream(Paths.get(path))) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            readFile(os, is);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBase64(String path) {
        return toBase64(getByte(path));
    }

    public static InputStream getInputStream(String path) {
        ByteArrayInputStream is = new ByteArrayInputStream(getByte(path));
        return is;
    }

    public static void createFile(String path, byte[] bytes) {
        try (OutputStream os = Files.newOutputStream(Paths.get(path))) {
            os.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toBase64(byte[] bytes) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }


    public static byte[] base64To(String base64) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(base64);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    





}
