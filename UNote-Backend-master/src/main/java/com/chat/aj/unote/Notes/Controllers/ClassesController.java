package com.chat.aj.unote.Notes.Controllers;

import com.chat.aj.unote.Notes.DTO.ClassCreationDTO;
import com.chat.aj.unote.Notes.Entity.Classes;
import com.chat.aj.unote.Notes.Services.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/v1/classes")
public class ClassesController {
    private final ClassesService classService;

    @Autowired
    public ClassesController(ClassesService classService) {
        this.classService = classService;
    }

    @GetMapping()
    public ResponseEntity<?> getSpecificClass(@RequestParam String name){
        Classes units = classService.findClass(name);
        return ResponseEntity.ok(units);
    }

    @PostMapping()
    public ResponseEntity<?> createClass(@RequestBody ClassCreationDTO newClass){
        Long id = classService.createClass(newClass);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Classes>> getAllClasses(){
        return ResponseEntity.ok(classService.getAll());
    }
}
