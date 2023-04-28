package com.yo.yoshare.common.area;

import com.yo.yoshare.mbg.model.AreaData;
import lombok.Data;

@Data
public class Area {
    Province province;
    City city;
    Region region;


    @Data
    public class Province{
        int code;
        String value;
    }

    @Data
    public class City{
        int code;
        String value;
    }

    @Data
    public class Region{
        int code;
        String value;
    }

    public static Area.Province toProvince(AreaData areaData){
        Area area = new Area();
        Area.Province province = area.new Province();
        province.setCode(areaData.getCodeid());
        province.setValue(areaData.getCityname());
        return province;
    }

    public static Area.City toCity(AreaData areaData){
        Area area = new Area();
        Area.City city = area.new City();
        city.setCode(areaData.getCodeid());
        city.setValue(areaData.getCityname());
        return city;
    }

    public static Area.Region toRegion(AreaData areaData){
        Area area = new Area();
        Area.Region region = area.new Region();
        region.setCode(areaData.getCodeid());
        region.setValue(areaData.getCityname());
        return region;
    }
}
