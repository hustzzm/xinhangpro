package com.pig.modules.gt.controller;

import com.pig.basic.util.CommonResult;
import com.pig.basic.util.CommonUtil;
import com.pig.basic.util.utils.DateUtils;
import com.pig.modules.gt.dao.BizCompanyDao;
import com.pig.modules.gt.entity.BizBooking;
import com.pig.modules.gt.entity.BizCompany;
import com.pig.modules.gt.service.BizCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 房间管理(BizCompany)表控制层
 *
 * @author makejava
 * @since 2022-07-12 23:32:30
 */
@RestController
@Slf4j
@RequestMapping("/bizCompany")
public class BizCompanyController {

    @Value("${upload.file.path:}")
    private String uploadFilePath;
    /**
     * 服务对象
     */
    @Resource
    private BizCompanyService bizCompanyService;

    @Resource
    private BizCompanyDao bizCompanyDao;

    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {
        Page<BizCompany> bizCompanyPage = bizCompanyService.page(params);

        return CommonResult.ok(bizCompanyPage);
    }

    @PostMapping(value = "/save")
    public CommonResult save(@RequestBody BizCompany bizCompany) {
        if (CommonUtil.checkObjAllFieldsIsNull(bizCompany)) {
            return CommonResult.ok();
        }
        bizCompanyDao.save(bizCompany);
        return CommonResult.ok("新增成功");
    }

    @PostMapping(value = "/update")
    public CommonResult update(@RequestBody BizCompany bizCompany) {
        if (CommonUtil.checkObjAllFieldsIsNull(bizCompany)) {
            return CommonResult.ok();
        }
        bizCompanyService.update(bizCompany);
        return CommonResult.ok("修改成功");
    }

    @DeleteMapping(value = "/remove/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        bizCompanyDao.deleteById(id);
        return CommonResult.ok("删除成功");
    }

    @PostMapping(value = "/base64Upload",produces = "multipart/form-data")
    public String base64Upload(@RequestParam(value = "file") MultipartFile file,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
//        if (file.isEmpty()) {
//            return CommonResult.failed("文件为空!");
//        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
//        if (!suffixName.equals(".jpg") && !suffixName.equals(".png")) {
//            return CommonResult.failed("图片格式不正确，请上传jpg或png类型！");
//        }
//        long size = file.getSize();
//        if (size / (1024 * 1024) > 5) {
//            return CommonResult.failed("请上传不大于5M的图片！");
//        }
        String monthDate = DateUtils.getMonthDate(); // 年月
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(uploadFilePath + "/" + monthDate + "/" + fileName);
//        File dest = new File("D:\\usr\\local" + "\\" + monthDate + "\\" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("uploadImage is exception,", e.getMessage(), e);
            return "图片上传出现异常，" + e.getMessage();
//            return CommonResult.failed("图片上传出现异常，" + e.getMessage());
        }
        String filename = "/" + monthDate + "/" + fileName;
//        return CommonResult.ok(filename);
        return filename;
    }


}

