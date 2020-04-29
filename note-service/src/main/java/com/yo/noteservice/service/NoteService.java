package com.yo.noteservice.service;

import com.yo.noteservice.mongoModel.Note;
import com.yo.yoshare.common.api.CommonResult;

import java.util.List;

public interface NoteService {

    Note addNote(Note note, Long classId);

    void saveNote(Note note) throws IllegalAccessException;

    List<Note> getNotesById(String id);

    Note getUserNoteById(String userId, String noteId);

    CommonResult deleteUserNote(String userId, String noteId);

    Note getUserNoteByIdForReading(String userId, String noteId);
}
