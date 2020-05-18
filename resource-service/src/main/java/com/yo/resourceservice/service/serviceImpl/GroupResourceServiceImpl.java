package com.yo.resourceservice.service.serviceImpl;

import com.yo.resourceservice.bo.ResourceType;
import com.yo.resourceservice.feignInterface.NoteService;
import com.yo.resourceservice.service.GroupResourceService;
import com.yo.resourceservice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.common.api.ResultCode;
import com.yo.yoshare.mbg.mapper.*;
import com.yo.yoshare.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class GroupResourceServiceImpl implements GroupResourceService {
    @Autowired(required = false)
    CmsGroupResourceMapper groupResourceMapper;
    @Autowired(required = false)
    CmsMemberResourceClassficationMapper memberClassificationMapper;
    @Autowired
    NoteService noteService;
    @Autowired(required = false)
    CmsGroupFavoritePageMapper favoritePageMapper;
    @Autowired/*(required = false)*/
    CmsMemberFileMapper memberFileMapper;
    @Autowired
    GmsGroupMemberRelationshipMapper relationshipMapper;
    @Value("${file.rootDir.memberFile}")
    private  String MEMBER_FILE_DIR;
    @Autowired
    private HttpServletRequest request;

    @Override
    public CommonResult getGroupResources(Long userId, Long groupId, String type, Long classis) {
        GmsGroupMemberRelationshipExample relationshipExample = new GmsGroupMemberRelationshipExample();
        relationshipExample.createCriteria().andMemberIdEqualTo(userId).andGroupIdEqualTo(groupId);
        int count = relationshipMapper.countByExample(relationshipExample);
        if (0 >= count) {
            return CommonResult.failed("无权限");
        }
        CmsGroupResourceExample example = new CmsGroupResourceExample();
        CmsGroupResourceExample.Criteria cri = example.createCriteria().andGroupIdEqualTo(groupId);
        if (ResourceType.isContain(type)){
            cri.andTypeEqualTo(type);
        }
        if (null != classis && 0 < classis){
            cri.andClassificationEqualTo(classis);
        }
        return CommonResult.success(groupResourceMapper.selectByExample(example),"成功");
    }

    @Override
    public CommonResult delGroupResource(CmsGroupResource resource) {
        switch (resource.getType()){
            case "NOTE":{
                CommonResult result = noteService.delNote(resource.getByUserId().toString(), resource.getResourceRef());
                if (result.getCode()!= ResultCode.SUCCESS.getCode()) {
                    return CommonResult.failed("操作失败");
                }
                CmsGroupResourceExample example = new CmsGroupResourceExample();
                example.createCriteria().andByUserIdEqualTo(resource.getByUserId())
                        .andTypeEqualTo("NOTE")
                        .andIdEqualTo(resource.getId());
                groupResourceMapper.deleteByExample(example);
                return CommonResult.success("操作成功");
            }
            case "FAVORITE": {
                favoritePageMapper.deleteByPrimaryKey(Long.valueOf(resource.getResourceRef()));
                groupResourceMapper.deleteByPrimaryKey(resource.getId());
                return CommonResult.success("操作成功");
            }
            case "FILE": {
                Long id = Long.valueOf(resource.getResourceRef());
                CmsMemberFile fileInfo = memberFileMapper.selectByPrimaryKey(id);
                String name = fileInfo.getName();
                String hashString = fileInfo.getMd5();
                groupResourceMapper.deleteByPrimaryKey(resource.getId());
                memberFileMapper.deleteByPrimaryKey(id);
                CmsMemberFileExample example = new CmsMemberFileExample();
                example.createCriteria().andMd5EqualTo(hashString);
                List<CmsMemberFile> files = memberFileMapper.selectByExample(example);
                if (files.size() != 0){
                    return CommonResult.success("操作成功");
                }
                File fileDir = new File(MEMBER_FILE_DIR + hashString);
                File file = new File(fileDir + "/" + name);
                file.delete();
                if (fileDir.exists() && !fileDir.delete()){
                    return CommonResult.failed("服务异常");
                }
                return CommonResult.success("操作成功");
            }
            default: {
                //TODO
            }
        }
        return CommonResult.failed("资源类型不正确");
    }

    @Override
    public CommonResult addFavorite(Long groupId, String title, String introduction, String url) {
        String userId = request.getHeader("userId");
        CmsGroupFavoritePage page = new CmsGroupFavoritePage();
        page.setCreatedTime(new Date());
        page.setUrl(url);
        page.setMemberId(Long.parseLong(userId));
        favoritePageMapper.insertSelective(page);
        CmsGroupResource resource = new CmsGroupResource();
        resource.setByUserId(Long.parseLong(userId));
        resource.setGroupId(groupId);
        resource.setDateTime(new Date());
        resource.setDescription(introduction);
        resource.setType("FAVORITE");
        resource.setResourceRef(page.getId().toString());
        resource.setTitle(title);
        groupResourceMapper.insertSelective(resource);
        return CommonResult.success("操作成功", "操作成功");
    }

    @Override
    public CommonResult getWeb(Long groupId, Long webId) {
        CmsGroupFavoritePageExample example = new CmsGroupFavoritePageExample();
        example.createCriteria().andGroupidEqualTo(groupId).andIdEqualTo(webId);
        List list = favoritePageMapper.selectByExample(example);
        return CommonResult.success(list !=null ? list.get(0) : null, "操作成功");
    }


}
