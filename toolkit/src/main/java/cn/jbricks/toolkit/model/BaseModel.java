package cn.jbricks.toolkit.model;import cn.jbricks.toolkit.top.annotation.TopEntry;import cn.jbricks.toolkit.util.DateUtil;import com.fasterxml.jackson.annotation.JsonIgnore;import org.apache.commons.lang.StringUtils;import java.io.Serializable;import java.util.*;/** * User: kelen * Date: 2011.9.8 */@TopEntry()public class BaseModel implements Serializable {    public static final String SEPARATE_SEMICOLON = ";";    public static final String SEPARATE_COLON = ":";    public static final String SEPARATE_COMMA = ",";    protected Long uid;                       //唯一的数字Id    @JsonIgnore    protected String attributes;              //扩展属性    protected Integer options;                //扩展选项    @JsonIgnore    protected Integer dataStatus;             //数据状态，-1——未生效，1——有效，-2——废弃    protected Date gmtCreated;                //创建时间    protected Date gmtModified;               //修改时间    @JsonIgnore    protected Integer appId;                   //应用id    protected Integer type;                    //数据类型    protected Integer subType;                 //子数据类型    @JsonIgnore    protected Long offset = 0l;                //查询开始位置    @JsonIgnore    protected Integer rows = 20;               //查询数量    @JsonIgnore    protected Integer pagination = 1;          //查询页码    private String gmtCreatedStr;    private String gmtModifiedStr;    public Long getUid() {        return uid;    }    public void setUid(Long uid) {        this.uid = uid;    }    public Integer getType() {        return type;    }    public void setType(Integer type) {        this.type = type;    }    public Integer getSubType() {        return subType;    }    public void setSubType(Integer subType) {        this.subType = subType;    }    public String getAttributes() {        return attributes;    }    public void setAttributes(String attributes) {        this.attributes = attributes;    }    public Integer getOptions() {        return options;    }    public void setOptions(Integer options) {        this.options = options;    }    public Integer getDataStatus() {        return dataStatus;    }    public void setDataStatus(Integer dataStatus) {        this.dataStatus = dataStatus;    }    public Date getGmtCreated() {        return gmtCreated;    }    public void setGmtCreated(Date gmtCreated) {        this.gmtCreated = gmtCreated;    }    public Date getGmtModified() {        return gmtModified;    }    public void setGmtModified(Date gmtModified) {        this.gmtModified = gmtModified;    }    public Integer getAppId() {        return appId;    }    public void setAppId(Integer appId) {        this.appId = appId;    }    public Long getOffset() {        return offset;    }    public Integer getRows() {        return rows;    }    public void setRows(Integer rows) {        if (rows != null) {            this.rows = rows;        }        calculateOffset();    }    public Integer getPagination() {        return pagination;    }    public void setPagination(Integer pagination) {        if (pagination != null && pagination.intValue() != 0) {            this.pagination = pagination;        }        calculateOffset();    }    private void calculateOffset() {        this.offset = Long.valueOf((this.pagination - 1) * this.rows);    }    public String getGmtCreatedStr() {        return DateUtil.format(gmtCreated);    }    public void setGmtCreatedStr(String gmtCreatedStr) {        this.gmtCreatedStr = gmtCreatedStr;    }    public String getGmtModifiedStr() {        return DateUtil.format(gmtModified);    }    public void setGmtModifiedStr(String gmtModifiedStr) {        this.gmtModifiedStr = gmtModifiedStr;    }    public List<String> valuesToList(String values) {        if (StringUtils.isNotEmpty(values)) {            return Arrays.asList(values.split(SEPARATE_SEMICOLON));        } else {            return new ArrayList<String>();        }    }    public String getAttribute(String key) {        return getAttributesMap().get(key);    }    @JsonIgnore    public Map<String, String> getAttributesMap() {        Map<String, String> map = new HashMap<String, String>();        if (StringUtils.isNotEmpty(this.attributes)) {            String[] attributeList = this.attributes.split(SEPARATE_COMMA);            if (attributeList != null) {                for (String item : attributeList) {                    if (StringUtils.isNotEmpty(item)) {                        String[] temp = item.split(SEPARATE_COLON);                        if (temp != null && temp.length == 2)                            map.put(temp[0], temp[1]);                    }                }            }        }        return map;    }    public void setAttribute(String key, String value) {        Map<String, String> map = getAttributesMap();        map.put(key, value);        setAttributesMap(map);    }    public void setAttributesMap(Map<String, String> map) {        if (map == null) {            return;        }        final StringBuilder stringBuilder = new StringBuilder();        String[] keySet = map.keySet().toArray(new String[]{});        for (String key : keySet) {            String value = map.get(key);            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {                key = key.replaceAll(SEPARATE_COLON, "").replaceAll(SEPARATE_COMMA, "");                value = value.replaceAll(SEPARATE_COLON, "").replaceAll(SEPARATE_COMMA, "");                stringBuilder.append(key).append(SEPARATE_COLON).append(value).append(SEPARATE_COMMA);            }        }        this.attributes = stringBuilder.toString();    }    public static final void main(String[] args) {        BaseModel baseModel = new BaseModel();        Map<String, String> map = new HashMap<String, String>();        map.put("key1", "1111");        map.put("key2", "2222");        map.put("ke:y,3", "22,2:2");        baseModel.setAttributesMap(map);        System.out.println(baseModel.getAttributes());        map = baseModel.getAttributesMap();        System.out.println(map);    }}