package com.yo.noteservice.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.yo.noteservice.dao.NoteDao;
import com.yo.noteservice.model.Content;
import com.yo.noteservice.mongoModel.Note;
import com.yo.noteservice.service.NoteEsService;
import com.yo.noteservice.service.NoteService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceMapper;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    NoteDao noteDao;
    @Autowired(required = false)
    CmsMemberResourceMapper cmsMemberResourceMapper;
    @Autowired
    NoteEsService noteEsService;


    /**
     * 添加新笔记
     * */
    @Override
    public Note addNote(Note note, Long classId) {
        int index = note.getContents().length-1;
        Content content = note.getContents()[index];
        content.setId(UUID.randomUUID().toString());
        content.setTime(new Date());
//        content.setContent(htmlEncode(content.getContent()));
        Note result = noteDao.addNote(note);
        com.yo.noteservice.esModel.Note esObject = new com.yo.noteservice.esModel.Note();
        BeanUtils.copyProperties(note, esObject);
        noteEsService.index(esObject);
        if (result != null && result.getId()!=null) {
            CmsMemberResource memberResource = new CmsMemberResource();
            memberResource.setByUserId(Long.valueOf(note.getBy()));
            memberResource.setResourceRef(note.getId());
            memberResource.setType("NOTE");
            memberResource.setDatetime(new Date());
            memberResource.setTitle(note.getTitle());
            String str = content.getContent();
            memberResource.setDescription(str.substring(0, str.length()<101 ? str.length() : 100));
            memberResource.setClassification(classId==null? "-1" : String.valueOf(classId));
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
//        content.setContent(htmlEncode(content.getContent()));
        content.setTime(new Date());
        content.setId(UUID.randomUUID().toString());
        noteDao.addElement(note.getId(),note.getBy(),"contents", content);
        note.setComments(null);
        note.setContents(null);
        note.setTags(null);
        noteDao.update(note);
        Note result = noteDao.getNote(note.getId());
        com.yo.noteservice.esModel.Note esNote = new com.yo.noteservice.esModel.Note();
        BeanUtils.copyProperties(result, esNote);
        noteEsService.update(esNote);
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
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CommonResult deleteUserNote(String userId, String noteId) throws IllegalAccessException {
        Note note = noteDao.getUserNoteById(userId, noteId);
        note.setStatus("0");
        noteDao.update(note);
        System.out.println("已删除");
        return CommonResult.success("操作成功");
    }


    @Override
    public boolean commitDelete(BusinessActionContext context) {
        String noteId = (String) context.getActionContext("noteId");
        Note note = noteDao.getNote(noteId);
        if ("0".equals(note.getStatus())){
            DeleteResult result = noteDao.remove(note);
            if (result.wasAcknowledged()){
                noteEsService.delete(noteId);
            }
        }
        System.out.println("提交");
        return true;
    }

    @Override
    public boolean rollbackDelete(BusinessActionContext context) throws IllegalAccessException {
        String noteId = (String) context.getActionContext("noteId");
        Note note = noteDao.getNote(noteId);
        note.setStatus("1");
        noteDao.update(note);
        System.out.println("回滚");
        return true;
    }

    @Override
    public Note getUserNoteByIdForReading(String userId, String noteId) {
        Note note = noteDao.getUserNoteById(userId, noteId);
        Content[] contents = note.getContents();
        Stream<Content> stream = Arrays.stream(contents).filter(
                Content::isPublished
        );
        note.setContents((Content[]) stream.toArray());
        return note;
    }

    private static String htmlEncode(char c) {
        switch(c) {
            case '&':
                return"&amp;";
            case '<':
                return"&lt;";
            case '>':
                return"&gt;";
            case '"':
                return"&quot;";
            case ' ':
                return"&nbsp;";
            default:
                return c +"";
        }
    }

    /** 对传入的字符串str进行Html encode转换 */
    public static String htmlEncode(String str) {
        if(str ==null || str.trim().equals("")) {
            return str;
        }
        StringBuilder encodeStrBuilder = new StringBuilder();
        for (int i = 0, len = str.length(); i < len; i++) {
            encodeStrBuilder.append(htmlEncode(str.charAt(i)));
        }
        return encodeStrBuilder.toString();
    }

}
