package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.SellerService")
@Transactional
public class SellerServiceImpl implements SellerService{

    @Autowired
    private SellerMapper sellerMapper;
    //添加商家
    @Override
    public void save(Seller seller) {
        try {
            seller.setStatus("0");
            seller.setCreateTime(new Date());
            sellerMapper.insertSelective(seller);
        } catch (Exception e) {
            throw new RuntimeException("商家申请入驻出现异常",e);
        }
    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Seller findOne(Serializable id) {
        return sellerMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(Seller seller, int page, int rows) {
        try {
            //开始分页
            PageInfo<Seller> pageInfo = PageHelper.startPage(page,rows)
                    .doSelectPageInfo(new ISelect(){
                @Override
                public void doSelect() {
                    Example example = new Example(Seller.class);
                    Example.Criteria criteria = example.createCriteria();
                    //审核状态码: 0 未审核
                    if (seller != null && !StringUtils.isEmpty(seller.getStatus())) {
                        criteria.andEqualTo("status", seller.getStatus());
                    }
                    //公司名称
                    if (seller != null && !StringUtils.isEmpty(seller.getName())){
                        Example.Criteria name = criteria.andLike("name", "%" + seller.getName() + "%");
                    }
                    //店铺名称
                    if (seller != null && !StringUtils.isEmpty(seller.getNickName())){
                        criteria.andLike("nickName","%"+ seller.getNickName() +"%");
                    }
                    sellerMapper.selectByExample(example);
                }
            });
            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 修改商家状态 */
    @Override
    public void updateStatus(String sellerId, String status) {
        try {
            Seller seller = new Seller();
            seller.setSellerId(sellerId);
            seller.setStatus(status);
            sellerMapper.updateByPrimaryKeySelective(seller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
