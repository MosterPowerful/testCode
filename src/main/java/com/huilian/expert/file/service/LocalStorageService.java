package com.huilian.expert.file.service;

import com.huilian.expert.common.bean.dto.FileUploadDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上传服务
 *
 * @author 赖卓成
 * @date 2023/03/14
 */
public interface LocalStorageService {
    /**
     * 上传
     *
     * @param file    文件
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return {@link FileUploadDto}
     */
    FileUploadDto upload(MultipartFile file,String bizType,String bizId);


    /**
     * 上传
     *
     * @param file    文件
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return {@link FileUploadDto}
     */
    FileUploadDto uploadVedio(MultipartFile file,String bizType,String bizId);


    /**
     * 上传
     *
     * @param file    文件
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return {@link FileUploadDto}
     */
    FileUploadDto uploadTemplate(MultipartFile file, String bizType, String bizId);

    /**
     * 根据路径删除文件
     *
     * @param path 路径
     */
    void removeByPath(String path);

    /**
     * 根据路径删除视频
     *
     * @param path 路径
     */
    void removeVideoPath(String path);

    /**
     * 通过路径下载文件
     *
     * @param path     路径
     * @param response 响应
     */
    void getByPath(String path, HttpServletResponse response);

    void getUploadTempByPath(String path, HttpServletResponse response);

    /**
     * 通过路径下载文件
     *
     * @param path     路径
     * @param response 响应
     */
    void getIconByPath(String path, HttpServletResponse response);

    void getVideoByPath(String path, HttpServletRequest request, HttpServletResponse response);

    /**
     * 通过路径
     *
     * @param path 路径
     * @return {@link ResponseEntity}<{@link Resource}>
     */
    ResponseEntity<Resource> getByPath(String path);

}
