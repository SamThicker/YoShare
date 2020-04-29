package com.yo.noteservice.service;

import com.yo.noteservice.mongoModel.GroupNote;
import com.yo.noteservice.mongoModel.Note;
import com.yo.yoshare.common.api.CommonResult;

import java.util.List;

public interface GroupNoteService {

    GroupNote addNote(GroupNote note, Long classId, Long groupId);

    void saveNote(GroupNote note) throws IllegalAccessException;

    List<GroupNote> getNotesByGroupId(String id);

    GroupNote getGroupNoteById(String groupId, String noteId);

    CommonResult deleteGroupNote(String userId, String noteId, String groupId);

    GroupNote getGroupNoteByIdForReading(String groupId, String noteId);

}
