package com.pinyougou.mapper;

import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.TypeTemplate;

import java.util.List;

/**
 * TypeTemplateMapper 数据访问接口
 * @date 2018-09-22 22:14:23
 * @version 1.0
 */
public interface TypeTemplateMapper extends Mapper<TypeTemplate>{

    /** 多条件查询类型模版 */
    List<TypeTemplate> findAll(TypeTemplate typeTemplate);
}