package com.pinyougou.shop.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
public class UploadController {

    /* 定义文件服务器访问url */
    @Value("${fileServerUrl}")
    private String fileServerUrl;

    /*文件上传*/
    @PostMapping("/upload")
    public Map<String,Object> upload(@RequestParam("file")MultipartFile multipartFile){
        Map<String,Object> data = new HashMap<>();
        data.put("status",500);
        try {
            //获取文件名
            String originalFilename = multipartFile.getOriginalFilename();

            /*======上传文件到FastDFS===========*/
            //获取配置文件,得到绝对路径
            String confFileName = this.getClass().getResource("/fastdfs_client.conf").getPath();
            //初始化客户端全局对象
            ClientGlobal.init(confFileName);
            //创建存储客户端对象
            StorageClient storageClient = new StorageClient();
            //上传文件到Fastdfs
            //第一个参数：获取上传文件的字节数组
            String[] arr =storageClient.upload_file(multipartFile.getBytes(),
                            FilenameUtils.getExtension(originalFilename),null);
            /** 拼接返回的 url 和 ip 地址，拼装成完整的 url */
            StringBuilder url = new StringBuilder(fileServerUrl);
            for (String s : arr) {
                url.append("/" + s);
            }
            System.out.println("url : " + url.toString());
            data.put("status",200);
            data.put("url",url.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
