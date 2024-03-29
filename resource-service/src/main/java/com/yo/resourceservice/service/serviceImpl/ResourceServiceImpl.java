package com.yo.resourceservice.service.serviceImpl;

import com.yo.resourceservice.bo.ResourceType;
import com.yo.resourceservice.feignInterface.FileService;
import com.yo.resourceservice.feignInterface.NoteService;
import com.yo.resourceservice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.common.api.ResultCode;
import com.yo.yoshare.mbg.mapper.CmsMemberFavoritePageMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberFileMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceClassficationMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceMapper;
import com.yo.yoshare.mbg.model.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired(required = false)
    CmsMemberResourceMapper memberResourceMapper;
    @Autowired(required = false)
    CmsMemberResourceClassficationMapper memberClassificationMapper;
    @Autowired
    NoteService noteService;
    @Autowired
    FileService fileService;
    @Autowired(required = false)
    CmsMemberFavoritePageMapper favoritePageMapper;
    @Autowired/*(required = false)*/
    CmsMemberFileMapper memberFileMapper;
    @Value("${file.rootDir.memberFile}")
    private  String MEMBER_FILE_DIR;

    @Override
    public List<CmsMemberResource> getResourcesForSelf(Long id, String type, String classification) {
        CmsMemberResourceExample example = new CmsMemberResourceExample();
        CmsMemberResourceExample.Criteria cri = example.createCriteria().andByUserIdEqualTo(id);
        if (ResourceType.isContain(type)){
            cri.andTypeEqualTo(type);
        }
        if (!"".equals(classification) && Integer.valueOf(classification)>0){
            cri.andClassificationEqualTo(classification);
        }
        return memberResourceMapper.selectByExample(example);
    }



    @Override
    @Transactional
    @GlobalTransactional(name = "my_test_tx_group", rollbackFor = Exception.class)
    public CommonResult delResourceForSelf(CmsMemberResource resource) throws Exception {
        switch (resource.getType()){
            case "NOTE":{
                CommonResult result = noteService.delNote(resource.getByUserId().toString(), resource.getResourceRef());
                if (result.getCode()!= ResultCode.SUCCESS.getCode()) {
                    return CommonResult.failed("操作失败");
                }
                CmsMemberResourceExample example = new CmsMemberResourceExample();
                example.createCriteria().andByUserIdEqualTo(resource.getByUserId())
                        .andTypeEqualTo("NOTE")
                        .andIdEqualTo(resource.getId());
                memberResourceMapper.deleteByExample(example);
                //test global transaction rollback in TCC mode
                //int i = 1/0;
                return CommonResult.success("操作成功");
            }
            case "FAVORITE": {
                favoritePageMapper.deleteByPrimaryKey(Long.valueOf(resource.getResourceRef()));
                memberResourceMapper.deleteByPrimaryKey(resource.getId());
                return CommonResult.success("操作成功");
            }
            case "FILE": {
                System.out.println("deleting");
                Long id = Long.valueOf(resource.getResourceRef());
                CmsMemberFile fileInfo = memberFileMapper.selectByPrimaryKey(id);
                String name = fileInfo.getName();
                String hashString = fileInfo.getMd5();
                CmsMemberFileExample example = new CmsMemberFileExample();
                example.createCriteria().andMd5EqualTo(hashString).andNameEqualTo(name);
                //如果只剩这一份关联的resource，则连同文件删除
                if (1 == memberFileMapper.countByExample(example)){
                    CommonResult result = fileService.deleteFile(String.valueOf(id));
                    System.out.println(result);
                    if (result.getCode() != ResultCode.SUCCESS.getCode()){
                        return CommonResult.failed("出错辣");
                    }
                }
                memberResourceMapper.deleteByPrimaryKey(resource.getId());
                return CommonResult.success("操作成功");
            }
            default: {
                //TODO
            }
        }
        return CommonResult.failed("资源类型不正确");
    }

    @Override
    public CommonResult addClassification(Long userId, String type, String classificationName) {
        if (!ResourceType.isContain(type)){
            return CommonResult.failed("资源类型错误");
        }
        CmsMemberResourceClassficationExample example = new CmsMemberResourceClassficationExample();
        example.createCriteria().andMemberIdEqualTo(userId).andClassificationNameEqualTo(classificationName).andTypeEqualTo(type);
        List list = memberClassificationMapper.selectByExample(example);
        if (list.size()>0){
            return CommonResult.failed("已经存在该分类，请更换名称");
        }
        example.clear();
        example.createCriteria().andMemberIdEqualTo(userId).andTypeEqualTo(type);
        List all = memberClassificationMapper.selectByExample(example);
        if (all.size()>=20){
            return CommonResult.failed("仅允许创建20个自定分类");
        }
        CmsMemberResourceClassfication classfication = new CmsMemberResourceClassfication();
        classfication.setMemberId(userId);
        classfication.setClassificationName(classificationName);
        classfication.setType(type);
        memberClassificationMapper.insertSelective(classfication);
        return CommonResult.success("操作成功");
    }

    @Override
    public CommonResult addFavorite(Long userId, String title, String introduction, String url, String classis) {
        CmsMemberFavoritePage page = new CmsMemberFavoritePage();
        page.setCreatedTime(new Date());
        page.setUrl(url);
        page.setMemberId(userId);
        favoritePageMapper.insertSelective(page);
        CmsMemberResource resource = new CmsMemberResource();
        resource.setByUserId(userId);
        resource.setDatetime(new Date());
        resource.setDescription(introduction);
        resource.setType("FAVORITE");
        resource.setResourceRef(page.getId().toString());
        resource.setTitle(title);
        resource.setClassification(classis);
        memberResourceMapper.insertSelective(resource);
        return CommonResult.success("操作成功", "操作成功");
    }

    @Override
    public CommonResult getClassification(Long userId, String type) {
        CmsMemberResourceClassficationExample example = new CmsMemberResourceClassficationExample();
        if (!ResourceType.isContain(type)) {
            example.createCriteria().andMemberIdEqualTo(userId);
            return CommonResult.success(memberClassificationMapper.selectByExample(example), "操作成功");
        }
        example.createCriteria().andMemberIdEqualTo(userId).andTypeEqualTo(type);
        return CommonResult.success(memberClassificationMapper.selectByExample(example));
    }

    @Override
    public CommonResult updateClassification(Long userId, CmsMemberResourceClassfication memberResourceClassfication) {
        CmsMemberResourceClassficationExample example = new CmsMemberResourceClassficationExample();
        example.createCriteria().andMemberIdEqualTo(userId).andIdEqualTo(memberResourceClassfication.getId());
        memberClassificationMapper.updateByExampleSelective(memberResourceClassfication, example);
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult deleteClassification(Long userId, Long classificationId) {
        CmsMemberResourceClassficationExample example = new CmsMemberResourceClassficationExample();
        example.createCriteria().andIdEqualTo(classificationId).andMemberIdEqualTo(userId);
        memberClassificationMapper.deleteByExample(example);
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult getWeb(Long userId, Long webId) {
        CmsMemberFavoritePageExample example = new CmsMemberFavoritePageExample();
        example.createCriteria().andMemberIdEqualTo(userId).andIdEqualTo(webId);
        List list = favoritePageMapper.selectByExample(example);
        return CommonResult.success(list!=null ? list.get(0) : null, "操作成功");
    }

}
