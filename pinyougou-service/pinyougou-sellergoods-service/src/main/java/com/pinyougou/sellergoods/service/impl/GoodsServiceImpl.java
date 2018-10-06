package com.pinyougou.sellergoods.service.impl;
import java.util.Date;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.GoodsService")
@Transactional
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public void save(Goods goods) {
        try {
            //往tb_goods插入数据
            //设置状态码
            goods.setAuditStatus("0");
            goodsMapper.insertSelective(goods);
            //往tb_goods_desc插入数据
            //设置主键Id
            goods.getGoodsDesc().setGoodsId(goods.getId());
            goodsDescMapper.insertSelective(goods.getGoodsDesc());

            //判断是否启用规格
            if ("1".equals(goods.getIsEnableSpec())) {
                //往tb_item插入数据
                for (Item item : goods.getItems()) {
                    // items : [{spec:{}, price:0, num:9999,status:'0', isDefault:'0' }]
                    //设置商品的标题：SPU名称 + 规格选项
                    StringBuilder title = new StringBuilder(goods.getGoodsName());
                    //{"网络":"联通4G","机身内存":"64G"} -->Map集合
                    Map<String, String> specMap = JSON.parseObject(item.getSpec(), Map.class);
                    for (Map.Entry<String, String> entry : specMap.entrySet()) {
                        title.append(" " + entry.getValue());
                    }
                    item.setTitle(title.toString());
                    //设置SKU其他信息
                    setItemInfo(item, goods);
                    //往tb_item插入数据
                    itemMapper.insertSelective(item);
                }
            } else {
                //创建SKU商品
                Item item = new Item();
                // items : [{spec:{}, price:0, num:9999,status:'0', isDefault:'0' }]
                item.setSpec("{}");
                //设置价格
                item.setPrice(goods.getPrice());
                //设置数量
                item.setNum(1000);
                //设置状态码
                item.setStatus("1");
                //设置为默认
                item.setIsDefault("1");
                //设置标题
                item.setTitle(goods.getGoodsName());
                //设置SKU其他信息
                setItemInfo(item, goods);
                //往tb_item插入数据
                itemMapper.insertSelective(item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //设置SKU其他信息
    private void setItemInfo(Item item,Goods goods) {
        //设置SKU商品的图片
        List<Map> itemImages = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (itemImages != null && itemImages.size() > 0){
            String imageUrl = itemImages.get(0).get("url").toString();
            item.setImage(imageUrl);
        }
        //设置SKU商品的三级分类
        item.setCategoryid(goods.getCategory3Id());
        /** 设置SKU商品的创建时间 */
        item.setCreateTime(new Date());
        /** 设置SKU商品的修改时间 */
        item.setUpdateTime(item.getCreateTime());
        /** 设置SPU商品的编号 */
        item.setGoodsId(goods.getId());
        /** 设置商家编号 */
        item.setSellerId(goods.getSellerId());
        /** 设置三级分类名称 */
        ItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
        item.setCategory(itemCat != null ? itemCat.getName() : "");
        /** 设置品牌名称 */
        Brand brand = brandMapper.selectByPrimaryKey(goods.getBrandId());
        item.setBrand(brand != null ? brand.getName() : "");
        /** 设置商家店铺名称 */
        Seller seller = sellerMapper.selectByPrimaryKey(goods.getSellerId());
        item.setSeller(seller != null ? seller.getNickName() : "");
    }

    @Override
    public void update(Goods goods) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {
        try {
            /** 修改删除状态 */
            goodsMapper.updateDeleteStatus(ids,"1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Goods findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Goods> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(Goods goods, int page, int rows) {
        try {
            //开启分页
            PageInfo<Map<String,Object>> pageInfo = PageHelper.startPage(page,rows)
                    .doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    goodsMapper.findAll(goods);
                }
            });
            //获取分页数据
            List<Map<String,Object>> goodsList = pageInfo.getList();
            for (Map<String, Object> map : goodsList) {
                //获取一级分类id
                Long category1Id = (Long) map.get("category1Id");
                //获取一级分类
                if (category1Id != null){
                    map.put("category1Name",itemCatMapper.selectByPrimaryKey(category1Id).getName());
                }

                //获取二级分类id
                Long category2Id = (Long) map.get("category2Id");
                //获取二级分类
                if (category2Id != null){
                    map.put("category2Name",itemCatMapper.selectByPrimaryKey(category2Id).getName());
                }

                //获取一级分类id
                Long category3Id = (Long) map.get("category3Id");
                //获取一级分类
                if (category3Id != null){
                    map.put("category3Name",itemCatMapper.selectByPrimaryKey(category3Id).getName());
                }
            }
            return new PageResult(pageInfo.getTotal(),goodsList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量修改状态 */
    @Override
    public void updateAuditStatus(Long[] ids, String status) {

        try {
            goodsMapper.updateAuditStatus(ids,status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 修改可销售状态 */
    @Override
    public void updateMarketable(Long[] ids, String status) {
        try {
            goodsMapper.updateMarketable(ids,status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
