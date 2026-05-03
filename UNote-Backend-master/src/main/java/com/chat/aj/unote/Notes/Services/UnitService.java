package com.chat.aj.unote.Notes.Services;

import com.chat.aj.unote.Exceptions.DuplicateResourceException;
import com.chat.aj.unote.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Notes.DTO.GetUnitDTO;
import com.chat.aj.unote.Notes.DTO.UnitCreationDTO;
import com.chat.aj.unote.Notes.Entity.Classes;
import com.chat.aj.unote.Notes.Entity.Unit;
import com.chat.aj.unote.Notes.Repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {
    private final UnitRepository unitRepository;
    private final ClassesService classesService;

    @Autowired
    public UnitService(UnitRepository unitRepository, ClassesService classesService) {
        this.unitRepository = unitRepository;
        this.classesService = classesService;
    }

    public Unit getUnits(GetUnitDTO unit){
        Optional<Unit> u = unitRepository.findByIdOrName(unit.getId(), unit.getName());
        if(u.isEmpty()) throw new ResourceNotFoundException("Unit not found");
        return u.get();
    }

    public Long createUnit(UnitCreationDTO unitNew) {
        Classes c = classesService.findClass(unitNew.getClassName());
        if (unitRepository.findByName(unitNew.getName()).isPresent()) throw new DuplicateResourceException("Unit with name exists");
        Unit unit = new Unit();
        unit.setName(unitNew.getName());
        unit.setMyClass(c);
        unitRepository.save(unit);
        return unit.getId();
    }

    public void deleteUnit(UnitCreationDTO uDTO){
        Optional<Unit> u = unitRepository.findByNameAndMyClass(uDTO.getName(), classesService.findClass(uDTO.getClassName()));
        if (u.isEmpty()) throw new ResourceNotFoundException("Not Found");
        unitRepository.delete(u.get());
    }

    public List<Unit> getAll() {
        return unitRepository.findAll();
    }
}
