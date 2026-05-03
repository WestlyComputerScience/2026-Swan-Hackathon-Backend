package com.chat.aj.unote.Notes.Repository;

import com.chat.aj.unote.Notes.Entity.PdfComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PdfRepository extends JpaRepository<PdfComment, Long> {
    Optional<PdfComment> findByCommentIdAndNote_Id(Long commentId, Long note_id);
    List<PdfComment> findByNoteId(Long noteId);
}
