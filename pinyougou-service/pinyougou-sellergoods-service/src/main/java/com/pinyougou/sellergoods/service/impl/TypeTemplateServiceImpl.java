package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.TypeTemplateService")
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService{

    /** 注入数据访问接口代理对象 */
    @Autowired
    private TypeTemplateMapper typeTemplateMapper;

    @Override
    public void save(TypeTemplate typeTemplate) {
        try {
            typeTemplateMapper.insertSelective(typeTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        try {
            typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {
        try {
            Example example = new Example(TypeTemplate.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id", Arrays.asList(ids));
            typeTemplateMapper.deleteByExample(example);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TypeTemplate findOne(Serializable id) {
        return null;
    }

    @Override
    public List<TypeTemplate> findAll() {
        return null;
    }

    /** 分页查询类型模版 */
    @Override
    public PageResult findByPage(TypeTemplate typeTemplate, int page, int rows) {

        try {
            /** 开始分页 */
            PageInfo<TypeTemplate> pageInfo = PageHelper.startPage(page,rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    typeTemplateMapper.findAll(typeTemplate);
                    /*
                    不写SQL语句的方法:
                    //创建示范对象
                    Example example = new Example(TypeTemplate.class);
                    //创建条件对象
                    Example.Criteria criteria = example.createCriteria();
                    //判断
                    if (typeTemplate != null && StringUtils.isEmpty(typeTemplate.getName())){
                        //like条件
                        criteria.andLike("name","%"+typeTemplate.getName()+"%");
                    }
                    typeTemplateMapper.selectByExample(example);
                     */
                }
            });
            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
