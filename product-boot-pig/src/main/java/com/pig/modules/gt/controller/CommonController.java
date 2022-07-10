package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = "/base")
public class CommonController {
    @PostMapping(value = "/base64Upload")
    public CommonResult uploadImage(@RequestParam(value = "file") MultipartFile file,
                                    @RequestParam(value = "file") String id,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        if (file.isEmpty()) {
            return CommonResult.failed("文件为空!");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        if (!suffixName.equals(".jpg") && !suffixName.equals(".png")) {
            return CommonResult.failed("图片格式不正确，请上传jpg或png类型！");
        }
        long size = file.getSize();
        if (size / (1024 * 1024) > 5) {
            return CommonResult.failed("请上传不大于5M的图片！");
        }
        String filePath = "D://temp-rainy//"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("uploadImage is exception,", e.getMessage(), e);
            return CommonResult.failed("图片上传出现异常，" + e.getMessage());
        }
        String filename = "/temp-rainy/" + fileName;
        return CommonResult.ok(filename);
    }
}
