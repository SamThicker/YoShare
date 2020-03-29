package com.yo.resourceservice.service;

import com.yo.yoshare.mbg.model.CmsMemberResource;

import java.util.List;

public interface ResourceService {
    List<CmsMemberResource> getResourcesForSelf(Long id);
}
