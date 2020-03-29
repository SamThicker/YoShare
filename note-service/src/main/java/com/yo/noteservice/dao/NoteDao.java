package com.yo.noteservice.dao;

import com.mongodb.client.result.DeleteResult;
import com.yo.noteservice.mongoModel.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class NoteDao {
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 添加文章
     * */
    public Note addNote(Note note) {
       return mongoTemplate.save(note,"note");
    }

    /**
     * 删除文章
     * */
    public DeleteResult reMove(Note note) {
        return mongoTemplate.remove(note, "note");
    }

    /**
     * 更新文章
     * */
    public void update(Note note) throws IllegalAccessException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(note.getId()).and("by").is(note.getBy()));
        note.setBy(null);
        Update update = new Update();
        Field[] fields = Note.class.getDeclaredFields();
        for (Field filed : fields){
            filed.setAccessible(true);
            Object temp = filed.get(note);
            if (temp != null){
                update.set(filed.getName(), temp);
            }
        }
        mongoTemplate.updateFirst(query, update, Note.class, "note");
    }

    /**
     * 添加数组元素
     * */
    public void addElement(String id, String by, String key, Object object) throws IllegalAccessException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id).and("by").is(by));
        Update update = new Update();
        update.addToSet(key,object);
        mongoTemplate.updateFirst(query,update,Note.class,"note");
    }

    /**
     * 通过用户id获取所有笔记
     * */
    public List<Note> getNotesById(String id) {
        Query query = new Query(Criteria.where("by").is(id));
        return mongoTemplate.find(query, Note.class, "note");
    }


    public Note getUserNoteById(String userId, String noteId) {
        Query query = new Query(Criteria.where("by").is(userId).and("id").is(noteId));
        return mongoTemplate.findOne(query, Note.class, "note");
    }
}
