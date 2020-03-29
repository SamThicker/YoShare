package com.yo.noteservice.controller;

import com.yo.noteservice.mongoModel.Note;
import com.yo.noteservice.service.NoteService;
import com.yo.yoshare.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {
    @Autowired
    NoteService noteService;

    /**
     * 添加新笔记
     * */
    @PostMapping(value = "/addNote/{id}")
    public CommonResult addNote(@RequestBody Note note, @PathVariable(value = "id") String id) {
        note.setBy(id);
        Note result = noteService.addNote(note);
        return CommonResult.success(result, "操作成功");
    }

    /**
     * 更新笔记
     * */
    @PostMapping(value = "/updateNote/{id}")
    public CommonResult updateNote(@RequestBody Note note, @PathVariable(value = "id") String id) throws IllegalAccessException {
        note.setBy(id);
        if(note.getId() == null){
            return addNote(note, id);
        }
        noteService.saveNote(note);
        return CommonResult.success("操作成功");
    }

    @GetMapping(value = "/getNotes/{id}")
    public CommonResult<List> getNotes(@PathVariable(value = "id") String id) {
        List<Note> noteList =  noteService.getNotesById(id);
        return CommonResult.success(noteList, "操作成功");
    }

    @GetMapping(value = "/getNote/{userId}/{noteId}")
    public CommonResult<Note> getNotes(@PathVariable(value = "userId") String userId,
                                       @PathVariable(value = "noteId") String noteId) {
        Note note =  noteService.getUserNoteById(userId, noteId);
        return CommonResult.success(note, "操作成功");
    }


}
