package com.pinyougou.mapper;

import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.Brand;

import java.util.List;

/**
 * BrandMapper 数据访问接口
 * @date 2018-09-22 22:14:23
 * @version 1.0
 */
public interface BrandMapper extends Mapper<Brand>{

    List<Brand> findAll(Brand brand);
}