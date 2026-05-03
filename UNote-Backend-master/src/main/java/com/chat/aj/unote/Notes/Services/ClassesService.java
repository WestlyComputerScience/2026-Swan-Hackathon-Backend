package com.chat.aj.unote.Notes.Services;

import com.chat.aj.unote.Exceptions.DuplicateResourceException;
import com.chat.aj.unote.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Notes.DTO.ClassCreationDTO;
import com.chat.aj.unote.Notes.Entity.Classes;
import com.chat.aj.unote.Notes.Repository.ClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassesService {
    private final ClassesRepository classesRepository;

    @Autowired
    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    public Long createClass(ClassCreationDTO newClass) {
        if(!classesRepository.findByCodeOrName(newClass.getCode(), newClass.getName()).isEmpty()){
            throw new DuplicateResourceException("Class by this code or name exists");
        }

        Classes classNew = new Classes();
        classNew.setCode(newClass.getCode());
        classNew.setName(newClass.getName());
        if(newClass.getProfessor() != null) classNew.setProfessor(newClass.getProfessor());
        if(newClass.getYear() != null) classNew.setYear(newClass.getYear());
        classesRepository.save(classNew);
        return classNew.getId();
    }

    public Classes findClass(String name) {
        return classesRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + name));
    }

    public List<Classes> getAll() {
        return classesRepository.findAll();
    }
}
