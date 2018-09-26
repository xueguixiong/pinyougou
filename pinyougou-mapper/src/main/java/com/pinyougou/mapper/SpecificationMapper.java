package com.pinyougou.mapper;

import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.Specification;

import java.util.List;

/**
 * SpecificationMapper 数据访问接口
 * @date 2018-09-22 22:14:23
 * @version 1.0
 */
public interface SpecificationMapper extends Mapper<Specification>{

    /** 多条件查询规格 */
    List<Specification> findAll(Specification specification);
}