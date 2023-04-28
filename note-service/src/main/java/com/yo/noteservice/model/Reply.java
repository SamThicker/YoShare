package com.yo.noteservice.model;

import lombok.Data;
import java.util.Date;

@Data
public class Reply {
    private String id;
    private String by;
    private Date time;
    private boolean toComment;
    private String content;
    private String to;
}
