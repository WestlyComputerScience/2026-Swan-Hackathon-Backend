package com.chat.aj.unote.Notes.DTO;

import com.chat.aj.unote.Notes.NoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkCreationDTO {
    private String title;
    private String url;
    private NoteType type;  // YT, WEBSITE
    private Long unitId;
}
