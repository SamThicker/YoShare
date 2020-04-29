package com.yo.noteservice.dto;

import com.yo.noteservice.mongoModel.Note;
import lombok.Data;

@Data
public class NoteRes {
    private Note note;
    private Long classId;
}
