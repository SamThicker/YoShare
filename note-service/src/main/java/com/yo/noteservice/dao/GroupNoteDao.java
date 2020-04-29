package com.yo.noteservice.dao;

import com.mongodb.client.result.DeleteResult;
import com.yo.noteservice.mongoModel.GroupNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class GroupNoteDao {
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 添加文章
     * */
    public GroupNote addNote(GroupNote note) {
       return mongoTemplate.save(note,"groupNote");
    }

    /**
     * 根据笔记对象删除笔记
     * */
    public DeleteResult remove(GroupNote note) {
        return mongoTemplate.remove(note, "groupNote");
    }

    public DeleteResult remove(String userId, String noteId, String groupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(noteId).and("by").is(userId).and("groupId").is(groupId));
        return mongoTemplate.remove(query, GroupNote.class, "groupNote");
    }

    /**
     * 更新文章
     * */
    public void update(GroupNote note) throws IllegalAccessException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(note.getId()).and("by").is(note.getBy()).and("groupId").is(note.getGroupId()));
        note.setBy(null);
        Update update = new Update();
        Field[] fields = GroupNote.class.getDeclaredFields();
        for (Field filed : fields){
            filed.setAccessible(true);
            Object temp = filed.get(note);
            if (temp != null){
                update.set(filed.getName(), temp);
            }
        }
        mongoTemplate.updateFirst(query, update, GroupNote.class, "groupNote");
    }

    /**
     * 添加数组元素
     * */
    public void addElement(String id, String by, String key, Object object) throws IllegalAccessException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id).and("by").is(by));
        Update update = new Update();
        update.addToSet(key,object);
        mongoTemplate.updateFirst(query,update,GroupNote.class,"groupNote");
    }

    /**
     * 通过小组id获取所有笔记
     *
     * @return*/
    public List<GroupNote> getNotesByGroupId(String id) {
        Query query = new Query(Criteria.where("groupId").is(id));
        return mongoTemplate.find(query, GroupNote.class, "groupNote");
    }


    public GroupNote getGroupNoteById(String groupId, String noteId) {
        Query query = new Query(Criteria.where("groupId").is(groupId).and("id").is(noteId));
        return mongoTemplate.findOne(query, GroupNote.class, "groupNote");
    }
}
