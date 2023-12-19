package com.huilian.expert.file.service.impl;

import com.huilian.expert.common.bean.dto.FileUploadDto;
import com.huilian.expert.common.bean.exception.BusinessException;
import com.huilian.expert.common.config.ApplicationProperties;
import com.huilian.expert.common.constant.ExpertAuditConstant;
import com.huilian.expert.common.enums.BusinessExceptionEnum;
import com.huilian.expert.common.utils.FileUtil;
import com.huilian.expert.file.service.LocalStorageService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 上传服务实现类
 *
 * @author 赖卓成
 * @date 2023/03/14
 */
@Service
public class LocalStorageServiceImpl implements LocalStorageService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public FileUploadDto upload(MultipartFile file,String bizType,String bizId) {
        return this.commonUpload(applicationProperties.getFile().getPath(), bizType, bizId, file);
    }

    @Override
    public FileUploadDto uploadVedio(MultipartFile file, String bizType, String bizId) {
        return this.commonUpload(applicationProperties.getFile().getVideoPath(), bizType, bizId, file);
    }


    @Override
    public FileUploadDto uploadTemplate(MultipartFile file, String bizType, String bizId) {
        return this.commonUpload(applicationProperties.getFile().getTemplate(), bizType, bizId, file);
    }

    @Override
    public void removeByPath(String path) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        String parentPath = applicationProperties.getFile().getPath();
        File file = new File(parentPath + ExpertAuditConstant.PATH_SEPARATOR + path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void removeVideoPath(String path) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        String parentPath = applicationProperties.getFile().getVideoPath();
        File file = new File(parentPath + ExpertAuditConstant.PATH_SEPARATOR + path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void getByPath(String path, HttpServletResponse response) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        String parentPath = applicationProperties.getFile().getPath();
        File file = new File(parentPath + ExpertAuditConstant.PATH_SEPARATOR + path);
        if (file.exists()) {
            try {
                FileUtil.download(response, parentPath + ExpertAuditConstant.PATH_SEPARATOR + path, file.getName().substring(file.getName().lastIndexOf('_') + 1));
            } catch (Exception e) {
                throw new BusinessException("下载文件失败");
            }

        }
    }

    @Override
    public void getUploadTempByPath(String path, HttpServletResponse response) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        String parentPath = applicationProperties.getFile().getTemplate();
        File file = new File(parentPath + ExpertAuditConstant.PATH_SEPARATOR + path);
        if (file.exists()) {
            try {
                FileUtil.download(response, parentPath + ExpertAuditConstant.PATH_SEPARATOR + path, file.getName().substring(file.getName().lastIndexOf('_') + 1));
            } catch (Exception e) {
                throw new BusinessException("下载文件失败");
            }

        }
    }

    @Override
    public void getIconByPath(String path, HttpServletResponse response) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        String parentPath = applicationProperties.getFile().getIconImage();
        File file = new File(parentPath + ExpertAuditConstant.PATH_SEPARATOR + path);
        if (file.exists()) {
            try {
                String fd = file.getName();
                FileUtil.download(response, parentPath + ExpertAuditConstant.PATH_SEPARATOR + path, file.getName());
            } catch (Exception e) {
                throw new BusinessException("下载文件失败");
            }

        }
    }

    @Override
    public void getVideoByPath(String path, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        String parentPath = applicationProperties.getFile().getVideoPath();
        File file = new File(parentPath + ExpertAuditConstant.PATH_SEPARATOR + path);
        System.out.println(file.getName());
        System.out.println(file.length());
        if (file.exists()) {
            try {
                String fd = file.getName();
                String rangeString = request.getHeader("Range");//如果是video标签发起的请求就不会为null

                long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));

                response.setHeader("Content-Type", "video/mp4");

                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName() , "UTF-8"));

                response.setContentLength(Integer.parseInt(file.length() + ""));//10000是视频文件的大小，上传文件时都会有这些参数的

                response.setHeader("Content-Range", String.valueOf(range + (file.length()-1)));//拖动进度条时的断点，其中10000是上面的视频文件大小，改成你的就好

                response.setHeader("Accept-Ranges", "bytes");

                response.setHeader("Etag", "W/\"9767057-1323779115364\"");//上传文件时都会有这些参数的
                FileUtil.download(response, parentPath + ExpertAuditConstant.PATH_SEPARATOR + path, file.getName());
            } catch (Exception e) {
//                throw new BusinessException("下载文件失败");
                String fd = file.getName();
                try {
                    FileUtil.download(response, parentPath + ExpertAuditConstant.PATH_SEPARATOR + path, file.getName());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    @Override
    public ResponseEntity<Resource> getByPath(String filePath) {
        String parentPath = applicationProperties.getFile().getPath();
        filePath = parentPath + ExpertAuditConstant.PATH_SEPARATOR + filePath;
        Path path = Paths.get(filePath);
        byte[] fileBytes = new byte[0];
        try {
            fileBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }
        ByteArrayResource resource = new ByteArrayResource(fileBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample" + filePath.substring(filePath.lastIndexOf(".")));
        // 获取文件的扩展名
        String fileExtension = FilenameUtils.getExtension(path.toString());

        // 根据文件类型设置Content-Type值
        switch (fileExtension.toLowerCase()) {
            case "txt":
                headers.setContentType(MediaType.TEXT_PLAIN);
                break;
            case "pdf":
                headers.setContentType(MediaType.APPLICATION_PDF);
                break;
            case "jpg":
            case "jpeg":
                headers.setContentType(MediaType.IMAGE_JPEG);
                break;
            case "png":
                headers.setContentType(MediaType.IMAGE_PNG);
                break;
            default:
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                break;
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileBytes.length)
                .body(resource);
    }

    /**
     * 选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
     *
     * @param multipartFile 文件
     * @return {@link File}
     */
    public File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (Exception e) {
            if (e instanceof  IllegalArgumentException){
                throw new BusinessException("文件名过短，请修改文件名后重新上传！");
            }
            e.printStackTrace();
        }
        return file;
    }


    private FileUploadDto commonUpload(String foldPath, String bizType, String bizId, MultipartFile file) {
        // 文件上传路径，判断路径是否存在目录，不存在则创建
        String path = foldPath + '/' + bizType + '/' + bizId + "/";
        File fileFolder = new File(path);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }

        File transferToFile = this.transferToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File newFile = new File(path + '/' + fileName);
        // 如果文件存在，则删除并覆盖
        if (newFile.exists()) {
            newFile.delete();
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(transferToFile), 1024 * 8);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile), 1024 * 8)) {
            byte[] dataByte = new byte[1024 * 8];
            int len = 0;
            while ((len = bis.read(dataByte)) != -1) {
                bos.write(dataByte, 0, len);
            }
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.FILE_SAVE_FAILED);
        }
        return new FileUploadDto().setRelativePath('/' + bizType + '/' + bizId + '/' + fileName);
    }
}
