package com.chat.aj.unote.Notes.request;

import lombok.Data;

@Data
public class CreatePdfCommentRequest {
    private int x;
    private int y;
    private int width;
    private int height;
    private String comment;
    private int pageNumber;
}
