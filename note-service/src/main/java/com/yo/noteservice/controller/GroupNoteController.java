package com.yo.noteservice.controller;

import com.yo.noteservice.dto.GroupNoteRes;
import com.yo.noteservice.mongoModel.GroupNote;
import com.yo.noteservice.service.GroupNoteService;
import com.yo.yoshare.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/note/group")
public class GroupNoteController {

    @Autowired
    private GroupNoteService groupNoteService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 添加新笔
     * */
    @PostMapping(value = "/note/{groupId}")
    public CommonResult addNote(@RequestBody GroupNoteRes noteRes, @PathVariable("groupId") Long groupId) {
        Long userId = Long.valueOf(request.getHeader("userId"));
        GroupNote note = noteRes.getNote();
        note.setBy(String.valueOf(userId));
        GroupNote result = groupNoteService.addNote(note, noteRes.getClassId(), groupId);
        return CommonResult.success(result, "操作成功");
    }

    /**
     * 更新笔记
     * */
    @PostMapping(value = "/updateNote/{groupId}")
    public CommonResult updateNote(@RequestBody GroupNoteRes noteRes, @PathVariable(value = "groupId") String groupId) throws IllegalAccessException {
        Long userId = Long.valueOf(request.getHeader("userId"));
        GroupNote note = noteRes.getNote();
        note.setBy(String.valueOf(userId));
        if(note.getId() == null){
            return addNote(noteRes, Long.valueOf(groupId));
        }
        groupNoteService.saveNote(note);
        return CommonResult.success("操作成功");
    }

    /**
     * 获取小组所有笔记
     * */
    @GetMapping(value = "/getNotes/{groupId}")
    public CommonResult<List> getNotes(@PathVariable(value = "groupId") String groupId) {
        List<GroupNote> noteList =  groupNoteService.getNotesByGroupId(groupId);
        return CommonResult.success(noteList, "操作成功");
    }

    /**
     * 获取小组特定笔记
     *
     * @return*/
    @GetMapping(value = "/getNote/{groupId}/{noteId}")
    @ResponseBody
    public CommonResult<GroupNote> getNotes(@PathVariable(value = "groupId") String groupId,
                                            @PathVariable(value = "noteId") String noteId) {
        GroupNote note =  groupNoteService.getGroupNoteById(groupId, noteId);
        return CommonResult.success(note, "操作成功");
    }

    /**
     * 获取特定用户特定笔记（阅读视图）
     *
     * @return*/
    @GetMapping(value = "/note/{userId}/{noteId}")
    @ResponseBody
    public CommonResult<GroupNote> getNotesForReading(@PathVariable(value = "userId") String userId,
                                                      @PathVariable(value = "noteId") String noteId) {
        GroupNote note =  groupNoteService.getGroupNoteByIdForReading(userId, noteId);
        return CommonResult.success(note, "操作成功");
    }

    /**
     * 删除特定笔记
     * */
    @RequestMapping(value = "/ownNote/{groupId}/{noteId}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult delNote(@PathVariable(value = "groupId") String groupId,
                                @PathVariable(value = "noteId") String noteId) {
        String userId = request.getHeader("userId");
        return groupNoteService.deleteGroupNote(userId, noteId, groupId);
    }

}
