package com.chat.aj.unote.Notes.request;

import lombok.Data;

@Data
public class UpdateTimestampRequest {
    private Double timestamp;
    private String comment;
}
