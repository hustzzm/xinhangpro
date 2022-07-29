package com.pig.modules.gt.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.pig.basic.util.CommonResult;
import com.pig.modules.gt.entity.BizCompany;
import com.pig.modules.gt.entity.BizMember;
import com.pig.modules.gt.entity.BizMemberVO;
import com.pig.modules.gt.entity.BizOrderExportVO;
import com.pig.modules.gt.service.BizCompanyService;
import com.pig.modules.gt.service.BizMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 用户信息
不可编辑：account，openid，register_time，avatar，nickname，gender(BizMember)表控制层
 *
 * @author makejava
 * @since 2022-07-10 13:36:52
 */
@RestController
@RequestMapping("bizMember")
public class BizMemberController {

    /**
     * 服务对象
     */
    @Resource
    private BizMemberService bizMemberService;
    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Map<String, Object> params) {

        //只有普通会员、钻石会员才加入列表
        params.put("checktype","0");
        Page<BizMember> bizCompanyPage = bizMemberService.page(params);

        return CommonResult.ok(bizCompanyPage);
    }

    /**
     * 会员信息更新操作
     */
    @PostMapping("/update")
    public CommonResult update(@RequestBody Map<String, Object> params) {
        return bizMemberService.insertOrUpdate(params);
    }

    /**
     * 会员拉黑、删除操作
     */
    @PostMapping("/updatestatus")
    public CommonResult updatestatus(@RequestBody Map<String, Object> params) {

        return bizMemberService.updateStatus(params);
    }

    @GetMapping("/export")
    public void export4(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String filenames = "会员列表";
        response.addHeader("Content-Disposition", "filename=" +
                java.net.URLEncoder.encode(filenames, "UTF-8") + ".xlsx");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BizMemberVO.class).build();
        WriteSheet[] writeSheet = new WriteSheet[]{
                EasyExcel.writerSheet(0, "sheet").build()
        };

        bizMemberService.exportData(s -> {
            BizMemberVO resultObject = s;
            ArrayList arrayList = new ArrayList<BizMemberVO>();
            arrayList.add(resultObject);
            excelWriter.write(arrayList, writeSheet[0]);
        });

        excelWriter.finish();

    }
}

