package com.pinyougou.mapper;

import com.pinyougou.pojo.Specification;
import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.SpecificationOption;

/**
 * SpecificationOptionMapper 数据访问接口
 * @date 2018-09-22 22:14:23
 * @version 1.0
 */
public interface SpecificationOptionMapper extends Mapper<SpecificationOption>{


    void save(Specification specification);
}