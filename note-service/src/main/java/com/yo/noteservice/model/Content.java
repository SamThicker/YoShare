package com.yo.noteservice.model;

import lombok.Data;
import java.util.Date;

@Data
public class Content {
    /**文章id*/
    private String id;
    /**创建时间*/
    private Date time;
    /**内容*/
    private String content;
    /**原始内容*/
    private String raw;
    /**标签*/
    private String tag;
    /**编辑器类型*/
    private String editor;
    /**该版本的笔记是否已经发布过*/
    private boolean published;

}
