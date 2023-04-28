package com.yo.noteservice.controller;

import com.yo.noteservice.dto.NoteRes;
import com.yo.noteservice.mongoModel.Note;
import com.yo.noteservice.service.NoteService;
import com.yo.yoshare.common.api.CommonResult;
import feign.RequestLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Autowired
    NoteService noteService;

    /**
     * 添加新笔记
     * */
    @PostMapping(value = "/addNote/{id}")
    public CommonResult addNote(@RequestBody NoteRes noteRes, @PathVariable(value = "id") String id) {
        Note note = noteRes.getNote();
        note.setBy(id);
        Note result = noteService.addNote(note, noteRes.getClassId());
        return CommonResult.success(result, "操作成功");
    }

    /**
     * 更新笔记
     * */
    @PostMapping(value = "/updateNote/{id}")
    public CommonResult updateNote(@RequestBody NoteRes noteRes, @PathVariable(value = "id") String id) throws IllegalAccessException {
        Note note = noteRes.getNote();
        note.setBy(id);
        if(note.getId() == null){
            return addNote(noteRes, id);
        }
        noteService.saveNote(note);
        return CommonResult.success("操作成功");
    }

    /**
     * 获取用户所有笔记
     * */
    @GetMapping(value = "/getNotes/{id}")
    public CommonResult<List> getNotes(@PathVariable(value = "id") String id) {
        List<Note> noteList =  noteService.getNotesById(id);
        return CommonResult.success(noteList, "操作成功");
    }

    /**搜索笔记*/
    @GetMapping(value = "/search")
    public CommonResult<List> searchNote(String keyWord) {
        return null;
    }

    /**
     * 获取特定用户特定笔记
     * */
    @GetMapping(value = "/getNote/{userId}/{noteId}")
    @ResponseBody
    public CommonResult<Note> getNotes(@PathVariable(value = "userId") String userId,
                                       @PathVariable(value = "noteId") String noteId) {
        Note note =  noteService.getUserNoteById(userId, noteId);
        return CommonResult.success(note, "操作成功");
    }

    /**
     * 获取特定用户特定笔记（阅读视图）
     * */
    @GetMapping(value = "/note/{userId}/{noteId}")
    @ResponseBody
    public CommonResult<Note> getNotesForReading(@PathVariable(value = "userId") String userId,
                                       @PathVariable(value = "noteId") String noteId) {
        Note note =  noteService.getUserNoteByIdForReading(userId, noteId);
        return CommonResult.success(note, "操作成功");
    }

    /**
     * 删除特定用户特定笔记
     * */
    @RequestMapping(value = "/ownNote/{userId}/{noteId}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult delNote(@PathVariable(value = "userId") String userId,
                                @PathVariable(value = "noteId") String noteId) throws IllegalAccessException {
        return noteService.deleteUserNote(userId, noteId);
    }


}
