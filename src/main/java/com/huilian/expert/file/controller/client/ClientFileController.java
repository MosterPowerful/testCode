package com.huilian.expert.file.controller.client;

import com.huilian.expert.file.service.LocalStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 客户端文件控制器
 *
 * @author 赖卓成
 * @date 2023/03/16
 */
@RestController
@Api(tags = "文件服务-客户端调用")
@RequestMapping("/file/client")
public class ClientFileController {

    @Autowired
    private LocalStorageService localStorageService;

    @GetMapping("/download")
    @ApiOperation("文件服务-根据路径下载")
    public void getByPath(@RequestParam("path") String path, HttpServletResponse response) {
        localStorageService.getByPath(path,response);
    }

}
