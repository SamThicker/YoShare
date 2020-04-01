package com.yo.noteservice.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.yo.noteservice.dao.NoteDao;
import com.yo.noteservice.model.Content;
import com.yo.noteservice.mongoModel.Note;
import com.yo.noteservice.service.NoteService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceMapper;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    NoteDao noteDao;
    @Autowired(required = false)
    CmsMemberResourceMapper cmsMemberResourceMapper;


    /**
     * 添加新笔记
     * */
    @Override
    public Note addNote(Note note) {
        int index = note.getContents().length-1;
        Content content = (Content)note.getContents()[index];
        content.setId(UUID.randomUUID().toString());
        content.setTime(new Date());
        Note result = noteDao.addNote(note);
        if (result != null && result.getId()!=null) {
            CmsMemberResource memberResource = new CmsMemberResource();
            memberResource.setByUserId(Long.valueOf(note.getBy()));
            memberResource.setResourceRef(note.getId());
            memberResource.setType("NOTE");
            memberResource.setDatetime(new Date());
            memberResource.setTitle(note.getTitle());
            String str = content.getContent();
            memberResource.setDescription(str.substring(0, str.length()<101 ? str.length() : 100));
            cmsMemberResourceMapper.insertSelective(memberResource);
        }
        return result;
    }

    /**
     * 保存笔记
     * */
    @Override
    public void saveNote(Note note) throws IllegalAccessException {
        Content[] contents = note.getContents();
        if (contents.length <= 0 ) {
            return;
        }
        Content content = contents[0];
        content.setTime(new Date());
        content.setId(UUID.randomUUID().toString());
        noteDao.addElement(note.getId(),note.getBy(),"contents", content);
        note.setComments(null);
        note.setContents(null);
        note.setTags(null);
        noteDao.update(note);
    }

    @Override
    public List<Note> getNotesById(String id) {
        return noteDao.getNotesById(id);
    }

    @Override
    public Note getUserNoteById(String userId, String noteId) {
        return  noteDao.getUserNoteById(userId, noteId);
    }

    @Override
    public CommonResult deleteUserNote(String userId, String noteId) {
        DeleteResult result = noteDao.remove(userId, noteId);
        if (result.wasAcknowledged()){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败，请重试");
    }


}