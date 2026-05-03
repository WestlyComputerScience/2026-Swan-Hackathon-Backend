package com.chat.aj.unote.Notes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUnitDTO {
    public String name;
    public Long id;

    public GetUnitDTO(String name) {
        this.name = name;
    }

    public GetUnitDTO(Long id) {
        this.id = id;
    }
}
