package com.framework.center.controller;

import com.framework.common.api.controller.BaseController;
import com.framework.common.api.entity.Kv;
import com.framework.common.api.entity.R;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.api.file.LocalFile;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>文件上传类</p>
 *
 * @author : gao
 * @date : 2024-01-31 14:35
 **/



@RestController
@Api(
        value = "文件管理",
        tags = {"文件管理"}
)
@ApiSupport(
        order = 19
)
public class FileController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @PostMapping({"/admin/upload/file"})
    @ApiOperationSupport(
            order = 1
    )
    @ApiOperation(
            value = "上传文件获得地址和名称",
            notes = "上传文件获得地址和名称"
    )
    public R<Kv> importUser(@ApiParam("上传文件") @RequestPart @RequestParam MultipartFile file) {
        LocalFile localFile = this.getFile(file);
        log.info("收到上传文件的请求：即将保存到：" + localFile.getUploadPath());
        localFile.save();
        File target = new File(localFile.getUploadPath());
        if (target.exists()) {
            log.info(target.getPath() + " 文件存在,虚拟目录：" + localFile.getUploadVirtualPath());
            Kv kv = Kv.create();
            kv.set("name", localFile.getOriginalFileName()).set("url", localFile.getUploadVirtualPath());
            return R.data(kv);
        } else {
            throw new ServiceException("文件保存失败，未能上传成功，请您稍后再试！");
        }
    }

    @Autowired
    public FileController() {
    }
}


