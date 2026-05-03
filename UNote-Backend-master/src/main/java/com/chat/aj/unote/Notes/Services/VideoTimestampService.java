package com.chat.aj.unote.Notes.Services;

import com.chat.aj.unote.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Notes.DTO.VideoTimestampDto;
import com.chat.aj.unote.Notes.Entity.Notes;
import com.chat.aj.unote.Notes.NoteType;
import com.chat.aj.unote.Notes.Repository.NotesRepository;
import com.chat.aj.unote.Notes.request.CreateTimestampRequest;
import com.chat.aj.unote.Notes.Entity.VideoTimestamp;
import com.chat.aj.unote.Notes.Repository.VideoTimestampRepository;
import com.chat.aj.unote.Notes.request.UpdateTimestampRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class VideoTimestampService {
    private final VideoTimestampRepository repository;
    private final NotesRepository notesRepository;

    public List<VideoTimestampDto> getAllVideoTimestamps(Long notesId) {
        Notes note = getVideoNote(notesId);
        return repository.findByNoteId(note.getId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public VideoTimestampDto createVideoTimestamp(Long notesId, CreateTimestampRequest request, String username) {
        Notes note = getVideoNote(notesId);

        VideoTimestamp timestamp = new VideoTimestamp();
        timestamp.setTimestamp(request.getTimestamp());
        timestamp.setComment(request.getComment());
        timestamp.setUsername(username);
        timestamp.setNote(note);
        return mapToDto(repository.save(timestamp));
    }

    public VideoTimestampDto updateVideoTimestamp(Long notesId, Long commentId,
                                                  UpdateTimestampRequest request, String username) {
        getVideoNote(notesId);

        VideoTimestamp existing = repository
                .findByCommentIdAndNote_Id(commentId, notesId)
                .orElseThrow(() -> new ResourceNotFoundException("Timestamp not found"));

        if (!existing.getUsername().equals(username)) {
            throw new AccessDeniedException("You cannot update another user's timestamp");
        }

        existing.setTimestamp(request.getTimestamp());
        existing.setComment(request.getComment());
        return mapToDto(repository.save(existing));
    }

    public void deleteVideoTimestamp(Long notesId, Long commentId, String username) {
        getVideoNote(notesId);

        VideoTimestamp existing = repository
                .findByCommentIdAndNote_Id(commentId, notesId)
                .orElseThrow(() -> new ResourceNotFoundException("Timestamp not found"));

        if (!existing.getUsername().equals(username)) {
            throw new AccessDeniedException("You cannot delete another user's timestamp");
        }

        repository.delete(existing);
    }

    private Notes getVideoNote(Long notesId) {
        Notes note = notesRepository.findById(notesId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        if (note.getType() != NoteType.VIDEO) {
            throw new AccessDeniedException("Timestamps only allowed for VIDEO notes");
        }

        return note;
    }

    private VideoTimestampDto mapToDto(VideoTimestamp timestamp) {
        VideoTimestampDto dto = new VideoTimestampDto();
        dto.setCommentId(timestamp.getCommentId());
        dto.setTimestamp(timestamp.getTimestamp());
        dto.setComment(timestamp.getComment());
        dto.setUsername(timestamp.getUsername());
        return dto;
    }
}