package com.yo.noteservice.mongoModel;

import com.yo.noteservice.model.Comment;
import com.yo.noteservice.model.Content;
import lombok.Data;

@Data
public class GroupNote {


    private String id;
    /**标题*/
    private String title;
    /**文章内容*/
    private Content[] contents;
    /**作者*/
    private String by;
    /**所属小组*/
    private String groupId;
    /**评论*/
    private Comment[] comments;
    /**文章标签*/
    private String[] tags;

}
