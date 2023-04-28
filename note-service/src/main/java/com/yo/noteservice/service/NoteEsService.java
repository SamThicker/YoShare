package com.yo.noteservice.service;

import com.yo.noteservice.esModel.GroupNote;
import com.yo.noteservice.esModel.Note;

public interface NoteEsService {

    /**创建索引（添加文档）*/
    void index(Note note);

    /**更新文档*/
    void update(Note note);

    /**删除文档*/
    void delete(String noteId);

    /**创建小组索引（添加文档）*/
    void indexGroup(GroupNote note);

    /**更新小组文档*/
    void updateGroup(GroupNote note);

    /**删除小组文档*/
    void deleteGroup(String noteId);
}
