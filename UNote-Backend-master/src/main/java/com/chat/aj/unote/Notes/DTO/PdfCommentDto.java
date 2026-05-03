package com.chat.aj.unote.Notes.DTO;

import lombok.Data;

@Data
public class PdfCommentDto {
    private Long commentId;
    private int x;
    private int y;
    private int width;
    private int height;
    private int pageNumber;
    private String comment;
    private String username;
}