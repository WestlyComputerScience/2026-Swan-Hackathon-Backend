package com.chat.aj.unote.Notes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextEntryDTO {
    public String content;
    public Long unitId;
    public String title;
}
