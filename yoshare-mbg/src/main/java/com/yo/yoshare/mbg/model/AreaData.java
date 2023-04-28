package com.yo.yoshare.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class AreaData implements Serializable {
    private Integer codeid;

    @ApiModelProperty(value = "父id")
    private Integer parentid;

    @ApiModelProperty(value = "省/市/县名称")
    private String cityname;

    private static final long serialVersionUID = 1L;

    public Integer getCodeid() {
        return codeid;
    }

    public void setCodeid(Integer codeid) {
        this.codeid = codeid;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", codeid=").append(codeid);
        sb.append(", parentid=").append(parentid);
        sb.append(", cityname=").append(cityname);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}