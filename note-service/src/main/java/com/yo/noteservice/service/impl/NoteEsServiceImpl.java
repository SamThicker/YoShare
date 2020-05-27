package com.yo.noteservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yo.noteservice.esModel.GroupNote;
import com.yo.noteservice.esModel.Note;
import com.yo.noteservice.service.NoteEsService;
import org.elasticsearch.action.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NoteEsServiceImpl implements NoteEsService {
    @Autowired
    ElasticsearchTemplate esTemplate;

    @Override
    public void index(Note note){
        IndexQuery query = new IndexQuery();
        query.setId(note.getId());
        query.setObject(note);
        esTemplate.index(query);
    }

    @Override
    public void update(Note note){
        UpdateQuery query = new UpdateQuery();
        Map object = JSONObject.parseObject(JSONObject.toJSONString(note), Map.class);
        UpdateRequest request = new UpdateRequest();
        request.doc(object);
        query.setUpdateRequest(request);
        query.setClazz(note.getClass());
        query.setType("note");
        query.setIndexName("yoshare-note");
        query.setId(note.getId());
        esTemplate.update(query);
    }

    @Override
    public void delete(String noteId) {
        esTemplate.delete(Note.class, noteId);
    }

    @Override
    public void indexGroup(GroupNote note){
        IndexQuery query = new IndexQuery();
        query.setId(note.getId());
        query.setObject(note);
        esTemplate.index(query);
    }

    @Override
    public void updateGroup(GroupNote note){
        UpdateQuery query = new UpdateQuery();
        Map object = JSONObject.parseObject(JSONObject.toJSONString(note), Map.class);
        UpdateRequest request = new UpdateRequest();
        request.doc(object);
        query.setUpdateRequest(request);
        query.setClazz(note.getClass());
        query.setType("groupNote");
        query.setIndexName("yoshare-groupnote");
        query.setId(note.getId());
        esTemplate.update(query);
    }

    @Override
    public void deleteGroup(String noteId) {
        esTemplate.delete(GroupNote.class, noteId);
    }

}
