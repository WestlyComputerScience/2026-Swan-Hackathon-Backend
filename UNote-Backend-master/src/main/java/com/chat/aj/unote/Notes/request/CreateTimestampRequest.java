package com.chat.aj.unote.Notes.request;

import com.chat.aj.unote.Notes.Entity.Notes;
import lombok.Data;

@Data
public class CreateTimestampRequest {
    private Double timestamp;
    private String comment;
    private Long noteId;
}
