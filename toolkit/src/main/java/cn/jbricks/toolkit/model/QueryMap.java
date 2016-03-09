package cn.jbricks.toolkit.model;

import java.util.HashMap;

/**
 * Created by kuiyuexiang on 15/12/23.
 */
public class QueryMap extends HashMap<String, Object> {

    private Integer appId;

    private Long offset = 0l;                //查询开始位置
    private Integer rows = 20;               //查询数量
    private Integer pagination = 1;          //查询页码

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
        super.put("appId", appId);
    }

    public Long getOffset() {
        return offset;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        if (rows != null) {
            this.rows = rows;
        }
        calculateOffset();
    }

    public Integer getPagination() {
        return pagination;
    }

    public void setPagination(Integer pagination) {
        if (pagination != null) {
            this.pagination = pagination;
        }
        calculateOffset();
    }

    private void calculateOffset() {
        this.offset = Long.valueOf((this.pagination - 1) * this.rows);
        putOffsetRows();
    }

    private void putOffsetRows() {
        super.put("offset", offset);
        super.put("rows", rows);
    }

    @Override
    public Object put(String key, Object value) {
        if (value != null && value instanceof String && ((String) value).length() == 0) {
            return super.put(key, null);
        }
        return super.put(key, value);
    }
}
