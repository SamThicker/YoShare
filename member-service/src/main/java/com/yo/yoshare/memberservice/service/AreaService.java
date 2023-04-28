package com.yo.yoshare.memberservice.service;

import com.yo.yoshare.common.area.Area;

import java.util.List;

public interface AreaService {

    List<Area.City> getAreaByCode(int code);
}
