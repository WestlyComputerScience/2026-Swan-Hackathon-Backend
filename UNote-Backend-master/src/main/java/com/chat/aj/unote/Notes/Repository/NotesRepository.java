package com.chat.aj.unote.Notes.Repository;

import com.chat.aj.unote.Notes.Entity.Notes;
import com.chat.aj.unote.Notes.Entity.Unit;
import com.chat.aj.unote.Notes.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByUnitIdAndType(Long id, NoteType type);
    List<Notes> findByUnitIdAndTypeIn(Long unitId, List<NoteType> types);
    List<Notes> findByUnit(Unit unit);
}
