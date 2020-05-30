package com.yo.noteservice.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.yo.noteservice.dao.GroupNoteDao;
import com.yo.noteservice.model.Content;
import com.yo.noteservice.mongoModel.GroupNote;
import com.yo.noteservice.mongoModel.Note;
import com.yo.noteservice.service.GroupNoteService;
import com.yo.noteservice.service.NoteEsService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.CmsGroupResourceMapper;
import com.yo.yoshare.mbg.model.CmsGroupResource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class GroupNoteServiceImpl implements GroupNoteService {

    @Autowired
    GroupNoteDao groupNoteDao;
    @Autowired(required = false)
    CmsGroupResourceMapper resourceMapper;
    @Autowired
    NoteEsService noteEsService;


    /**
     * 添加新笔记
     *
     * @return*/
    @Override
    @Transactional
    public GroupNote addNote(GroupNote note, Long classId, Long groupId) {
        int index = note.getContents().length-1;
        Content content = (Content)note.getContents()[index];
        content.setId(UUID.randomUUID().toString());
        content.setTime(new Date());
        GroupNote result = groupNoteDao.addNote(note);
        com.yo.noteservice.esModel.GroupNote esObject = new com.yo.noteservice.esModel.GroupNote();
        BeanUtils.copyProperties(note, esObject);
        noteEsService.indexGroup(esObject);
        if (result != null && result.getId()!=null) {
            CmsGroupResource resource = new CmsGroupResource();
            resource.setByUserId(Long.valueOf(note.getBy()));
            resource.setResourceRef(note.getId());
            resource.setType("NOTE");
            resource.setDateTime(new Date());
            resource.setTitle(note.getTitle());
            String str = content.getContent();
            resource.setDescription(str.substring(0, str.length()<101 ? str.length() : 100));
            resource.setClassification(classId == null ? -1 : classId);
            resource.setGroupId(groupId);
            resourceMapper.insertSelective(resource);
        }
        return result;
    }



    /**
     * 保存笔记
     * */
    @Override
    @Transactional
    public void saveNote(GroupNote note) throws IllegalAccessException {
        Content[] contents = note.getContents();
        if (contents.length <= 0 ) {
            return;
        }
        Content content = contents[0];
        content.setTime(new Date());
        content.setId(UUID.randomUUID().toString());
        groupNoteDao.addElement(note.getId(),note.getBy(),"contents", content);
        note.setComments(null);
        note.setContents(null);
        note.setTags(null);
        groupNoteDao.update(note);
        Note result = groupNoteDao.getNote(note.getId());
        com.yo.noteservice.esModel.GroupNote esNote = new com.yo.noteservice.esModel.GroupNote();
        BeanUtils.copyProperties(result, esNote);
        noteEsService.updateGroup(esNote);
    }

    @Override
    public List<GroupNote> getNotesByGroupId(String id) {
        return groupNoteDao.getNotesByGroupId(id);
    }

    @Override
    public GroupNote getGroupNoteById(String groupId, String noteId) {
        return  groupNoteDao.getGroupNoteById(groupId, noteId);
    }

    @Override
    public CommonResult deleteGroupNote(String userId, String noteId, String groupId) {
        DeleteResult result = groupNoteDao.remove(userId, noteId, groupId);
        if (result.wasAcknowledged()){
            noteEsService.deleteGroup(noteId);
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败，请重试");
    }

    @Override
    public GroupNote getGroupNoteByIdForReading(String groupId, String noteId) {
        GroupNote note = groupNoteDao.getGroupNoteById(groupId, noteId);
        Content[] contents = note.getContents();
        Stream<Content> stream = Arrays.stream(contents).filter(
                Content::isPublished
        );
        note.setContents((Content[]) stream.toArray());
        return note;
    }

}
