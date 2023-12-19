package com.huilian.expert.file.controller.client;

import com.huilian.expert.file.service.LocalStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 客户端文件控制器
 *
 * @author 赖卓成
 * @date 2023/03/16
 */
@RestController
@Api(tags = "文件服务-客户端调用")
@RequestMapping("/admin")
public class AdminFileController {

    @Autowired
    private LocalStorageService localStorageService;

    @GetMapping("/sys_icon/{fileName}")
    @ApiOperation("文件服务-根据路径下载")
    public void getByPath(@PathVariable("fileName") String path, HttpServletResponse response) {
        localStorageService.getIconByPath(path,response);
    }


    @GetMapping("/sys_video")
    @ApiOperation("文件服务-根据路径下载")
    public void getByVideoPath(@RequestParam("path") String path, HttpServletRequest request, HttpServletResponse response) {
        localStorageService.getVideoByPath(path,request,response);
    }

    @PostMapping("/download")
    @ApiOperation("文件服务-根据路径下载")
    public void getByFilePath(@RequestParam("path") String path, HttpServletResponse response) {
        localStorageService.getByPath(path,response);
    }

    @PostMapping("/downloadTemplate")
    @ApiOperation("文件服务-根据路径下载")
    public void getUploadTempByPath(@RequestParam("path") String path, HttpServletResponse response) {
        localStorageService.getUploadTempByPath(path, response);
    }
}
