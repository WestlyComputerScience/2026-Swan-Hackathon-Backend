package com.chat.aj.unote.Notes.Repository;

import com.chat.aj.unote.Notes.Entity.Classes;
import com.chat.aj.unote.Notes.Entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByIdOrName(Long id, String name);
    Optional<Unit> findByName(String name);

    Optional<Unit> findByNameAndMyClass(String name, Classes myClass);
}
