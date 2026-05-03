package com.chat.aj.unote.Notes.Controllers;

import com.chat.aj.unote.Notes.DTO.GetUnitDTO;
import com.chat.aj.unote.Notes.DTO.UnitCreationDTO;
import com.chat.aj.unote.Notes.Entity.Unit;
import com.chat.aj.unote.Notes.Services.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/app/v1/units")
public class UnitController {
    private final UnitService unitService;

    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public ResponseEntity<?> getUnits(@RequestBody GetUnitDTO unitRequest, Principal principal){
        Unit u = unitService.getUnits(unitRequest);
        return ResponseEntity.ok(u);
    }

    @PostMapping
    public ResponseEntity<?> createUnit(@RequestBody UnitCreationDTO unitNew){
        Long id = unitService.createUnit(unitNew);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUnit(@RequestBody UnitCreationDTO unitDelete){
        unitService.deleteUnit(unitDelete);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Unit>> getAll(){
        return ResponseEntity.ok(unitService.getAll());
    }
}
