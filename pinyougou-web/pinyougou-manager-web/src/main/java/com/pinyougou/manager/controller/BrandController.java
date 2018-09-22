package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Brand;
import com.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrandController {

    /**
     * 引用服务接口代理对象
     * timeout: 调用服务接口方法超时时间毫秒数
     * @Reference注解包名为：com.alibaba.dubbo.config.annotation.Reference
     */
    @Reference(timeout = 10000)
    private BrandService brandService;

    /**
     * 查看全部品牌
     * @return
     */
    @GetMapping("/brand/findAll")
    public List<Brand> findAll(){
        System.out.println("11111");
        return brandService.findAll();
    }
}
