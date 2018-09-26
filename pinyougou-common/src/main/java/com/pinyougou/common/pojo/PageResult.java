package com.pinyougou.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果实体类
 */
public class PageResult implements Serializable{
    //{"total":100,"row":[{},{}]}
    //总记录数
    private long total;
    //分页查询数据
    private List<?> rows;

    public PageResult() {
    }

    public PageResult(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}