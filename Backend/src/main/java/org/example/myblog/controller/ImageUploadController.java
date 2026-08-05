package org.example.myblog.controller;


import org.example.myblog.common.controller.BaseController;
import org.example.myblog.common.domain.AjaxResult;
import org.example.myblog.service.impl.ImageUploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
public class ImageUploadController extends BaseController {

    @Autowired
    private ImageUploadServiceImpl imageUploadServiceimpl;

    // 文件上传
    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("image") MultipartFile file) {
        try {
            String imageUrl = imageUploadServiceimpl.uploadImage(file);
            return AjaxResult.success(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(500, "上传失败：" + e.getMessage(), null);
        }
    }
}