package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Brand;
import com.pinyougou.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/brand")
public class BrandController {

    /**
     * 引用服务接口代理对象
     * timeout: 调用服务接口方法超时时间毫秒数
     * @Reference注解包名为：com.alibaba.dubbo.config.annotation.Reference
     */
    @Reference(timeout = 10000)
    private BrandService brandService;

    /**
     * 分页查询全部品牌
     * @return
     */
    @GetMapping("/findByPage")
    public PageResult findByPage(Brand brand,Integer page, Integer rows){
        //响应数据{"total":100,"row":[{},{}]},需要定义一个实体类
        if (brand != null && StringUtils.isNoneBlank(brand.getName())){
            try {
                brand.setName(new String(brand.getName().getBytes("iso8859-1"),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        //调用服务层分页查询
        return brandService.findByPage(brand,page,rows);
    }

    //添加品牌
    @PostMapping("/save")
    public boolean save(@RequestBody Brand brand){
        try {
            brandService.save(brand);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //修改品牌
    @PostMapping("/update")
    public boolean update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //修改品牌
    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try {
            brandService.deleteAll(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
