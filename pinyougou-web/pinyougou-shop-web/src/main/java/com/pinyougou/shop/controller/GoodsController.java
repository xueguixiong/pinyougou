package com.pinyougou.shop.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;
import com.pinyougou.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/*
    商品控制器
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 10000)
    private GoodsService goodsService;

    //添加商品
    @PostMapping("/save")
    public boolean save(@RequestBody Goods goods){
        try {
            //获取登录用户名（商家Id）
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.setSellerId(sellerId);
            goodsService.save(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*多条件分页查询商家的商品*/
    @GetMapping("/findByPage")
    public PageResult findByPage(Goods goods,Integer page,Integer rows){
        try {
            //获取登录用户名（商家Id）
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.setSellerId(sellerId);
            if (StringUtils.isNotEmpty(goods.getGoodsName())){
                goods.setGoodsName(new String(goods.getGoodsName().getBytes("ISO8859-1"),"UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsService.findByPage(goods,page,rows);
    }
}
