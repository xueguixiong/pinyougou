package com.pinyougou.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.Goods;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * GoodsMapper 数据访问接口
 * @date 2018-09-22 22:14:23
 * @version 1.0
 */
public interface GoodsMapper extends Mapper<Goods>{

    /* 多条件查询商品 */
    List<Map<String,Object>> findAll(Goods goods);

    /** 批量修改状态 */
    void updateAuditStatus(@Param("ids") Long[] ids, @Param("status") String status);

    /** 修改删除状态 */
    void updateDeleteStatus(@Param("ids") Serializable[] ids, @Param("isDelete") String s);

    /** 修改可销售状态 */
    void updateMarketable(@Param("ids") Long[] ids, @Param("status") String status);
}