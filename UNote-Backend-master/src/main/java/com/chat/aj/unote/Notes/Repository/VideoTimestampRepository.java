package com.chat.aj.unote.Notes.Repository;

import com.chat.aj.unote.Notes.Entity.VideoTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoTimestampRepository extends JpaRepository<VideoTimestamp, Long> {
    List<VideoTimestamp> findByNoteId(Long noteId);
    Optional<VideoTimestamp> findByCommentIdAndNote_Id(Long commentId, Long noteId);
}