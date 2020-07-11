package com.yo.noteservice.service;

import com.yo.noteservice.mongoModel.Note;
import com.yo.yoshare.common.api.CommonResult;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

import java.util.List;

@LocalTCC
public interface NoteService {

    /**添加新笔记*/
    Note addNote(Note note, Long classId);

    /**保存笔记*/
    void saveNote(Note note) throws IllegalAccessException;

    /**获取用户所有笔记*/
    List<Note> getNotesById(String id);

    /**获取用户某篇笔记*/
    Note getUserNoteById(String userId, String noteId);

    /**删除用户笔记*/
    @TwoPhaseBusinessAction(name = "doDelete", commitMethod = "commitDelete", rollbackMethod = "rollbackDelete")
    CommonResult deleteUserNote(String userId, @BusinessActionContextParameter(paramName = "noteId") String noteId) throws IllegalAccessException;

    boolean commitDelete(BusinessActionContext context);

    boolean rollbackDelete(BusinessActionContext context) throws IllegalAccessException;

    /**获取用户笔记（阅读视图，只包含最后一项改动内容）*/
    Note getUserNoteByIdForReading(String userId, String noteId);
}
