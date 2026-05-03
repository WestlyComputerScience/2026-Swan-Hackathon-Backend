package com.chat.aj.unote.Notes.Repository;

import com.chat.aj.unote.Notes.Entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {
    List<Classes> findByCodeOrName(String code, String name);
    Optional<Classes> findByName(String name);
    Optional<Classes> findByCode(String code);
}
