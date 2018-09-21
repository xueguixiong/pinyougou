package com.pinyougou.service;

import com.pinyougou.pojo.Brand;

import java.util.List;

public interface BrandService {

    /**
     * 查询全部品牌
     * @return
     */
    List<Brand> findAll();
}
