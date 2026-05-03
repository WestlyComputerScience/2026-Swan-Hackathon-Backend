package com.chat.aj.unote.Notes.DTO;

import lombok.Data;

@Data
public class VideoTimestampDto {
    private Long commentId;
    private double timestamp;
    private String comment;
    private String username;
}
