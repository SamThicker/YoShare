package com.yo.noteservice.dto;

import com.yo.noteservice.mongoModel.GroupNote;
import com.yo.noteservice.mongoModel.Note;
import lombok.Data;

@Data
public class GroupNoteRes {
    private GroupNote note;
    private Long classId;
}
