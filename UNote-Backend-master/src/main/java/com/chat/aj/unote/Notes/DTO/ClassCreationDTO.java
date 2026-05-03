package com.chat.aj.unote.Notes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassCreationDTO {
    public String name;
    public String code;
    public String professor;
    private Integer year;

    public ClassCreationDTO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public ClassCreationDTO(String name, String code, String professor) {
        this.name = name;
        this.code = code;
        this.professor = professor;
    }

    public ClassCreationDTO(String name, String code, Integer year) {
        this.name = name;
        this.code = code;
        this.year = year;
    }
}
