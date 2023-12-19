package com.huilian.expert.file.controller.feign;

import com.huilian.expert.common.bean.dto.FileUploadDto;
import com.huilian.expert.common.bean.response.Result;
import com.huilian.expert.file.service.LocalStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件控制层
 *
 * @author 赖卓成
 * @date 2023/03/14
 */
@RestController
@Api(tags = "文件服务-内部调用")
@RequestMapping("/file/feign")
public class ExportFileController {


    @Autowired
    private LocalStorageService localStorageService;

    @PostMapping("/upload")
    @ApiOperation("文件服务-文件上传")
    public Result<FileUploadDto> upload(@RequestPart(name = "file") MultipartFile file,@RequestParam("bizType") String bizType,@RequestParam("bizId") String bizId) throws IOException {
        FileUploadDto data = localStorageService.upload(file,bizType,bizId);
        return Result.success(data);
    }

    @PostMapping("/uploadVideo")
    @ApiOperation("文件服务-文件上传")
    public Result<FileUploadDto> uploadVideo(@RequestPart(name = "file") MultipartFile file,@RequestParam("bizType") String bizType,@RequestParam("bizId") String bizId) {
        FileUploadDto data = localStorageService.uploadVedio(file,bizType,bizId);
        return Result.success(data);
    }

    @PostMapping("/uploadTemplate")
    @ApiOperation("文件服务-文件上传")
    public Result<FileUploadDto> uploadTemplate(@RequestPart(name = "file") MultipartFile file,@RequestParam("bizType") String bizType,@RequestParam("bizId") String bizId) {
        FileUploadDto data = localStorageService.uploadTemplate(file,bizType, bizId);
        return Result.success(data);
    }

    @DeleteMapping("/delete")
    @ApiOperation("文件服务-根据路径删除")
    public Result<Void> removeByPath(@RequestParam("path") String path) {
        localStorageService.removeByPath(path);
        return Result.success(null);
    }

    @DeleteMapping("/deleteVideo")
    @ApiOperation("文件服务-根据路径删除")
    public Result<Void> removeVideoPath(@RequestParam("path") String path) {
        localStorageService.removeVideoPath(path);
        return Result.success(null);
    }

    @GetMapping("/download")
    @ApiOperation("文件服务-根据路径下载")
    public ResponseEntity<Resource> getByPath(@RequestParam("path") String path) {
      return  localStorageService.getByPath(path);
    }

    @GetMapping("/test")
    public Result<Void> test(){
        return Result.success(null);
    }
}
