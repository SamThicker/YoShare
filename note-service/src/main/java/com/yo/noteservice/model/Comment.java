package com.yo.noteservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private String id;
    private String by;
    private String content;
    private Date time;
    private Reply[] replyList;
}
