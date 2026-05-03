package com.chat.aj.unote.Notes.Services;

import com.chat.aj.unote.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Notes.DTO.PdfCommentDto;
import com.chat.aj.unote.Notes.Entity.Notes;
import com.chat.aj.unote.Notes.Entity.PdfComment;
import com.chat.aj.unote.Notes.NoteType;
import com.chat.aj.unote.Notes.Repository.NotesRepository;
import com.chat.aj.unote.Notes.Repository.PdfRepository;
import com.chat.aj.unote.Notes.request.CreatePdfCommentRequest;
import com.chat.aj.unote.Notes.request.UpdatePdfCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {
    private final PdfRepository pdfRepository;
    private final NotesRepository notesRepository;

    public List<PdfCommentDto> getAllPdfComments(Long notesId) throws ResourceNotFoundException {
        Notes note = getPdfNote(notesId);
        return pdfRepository.findByNoteId(note.getId()).stream().map(this::mapToDto).toList();
    }

    public PdfCommentDto createPdfComment(Long notesId, CreatePdfCommentRequest request, String username)
            throws ResourceNotFoundException {
        Notes note = getPdfNote(notesId);
        PdfComment comment = new PdfComment();
        comment.setComment(request.getComment());
        comment.setX(request.getX());
        comment.setY(request.getY());
        comment.setWidth(request.getWidth());
        comment.setHeight(request.getHeight());
        comment.setPageNumber(request.getPageNumber());
        comment.setUsername(username);
        comment.setNote(note);
        return mapToDto(pdfRepository.save(comment));
    }

    public PdfCommentDto updatePdfComment(Long notesId, Long commentId,
                                          UpdatePdfCommentRequest request, String username)
            throws ResourceNotFoundException, AccessDeniedException {

        getPdfNote(notesId);
        PdfComment existing = pdfRepository.findByCommentIdAndNote_Id(commentId, notesId)
                .orElseThrow(() -> new ResourceNotFoundException("PDF comment not found"));

        if (!existing.getUsername().equals(username)) {
            throw new AccessDeniedException("You cannot update another user's comment");
        }

        existing.setComment(request.getComment());
        existing.setX(request.getX());
        existing.setY(request.getY());
        existing.setWidth(request.getWidth());
        existing.setHeight(request.getHeight());
        existing.setPageNumber(request.getPageNumber());
        return mapToDto(pdfRepository.save(existing));
    }

    public void deletePdfComment(Long notesId, Long commentId, String username) throws ResourceNotFoundException,
            AccessDeniedException {
        getPdfNote(notesId);
        PdfComment existing = pdfRepository
                .findByCommentIdAndNote_Id(commentId, notesId)
                .orElseThrow(() -> new ResourceNotFoundException("PDF comment not found"));

        if (!existing.getUsername().equals(username)) {
            throw new AccessDeniedException("You cannot delete another user's comment");
        }

        pdfRepository.delete(existing);
    }

    private Notes getPdfNote(Long notesId) throws ResourceNotFoundException, AccessDeniedException {
        Notes note = notesRepository.findById(notesId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        if (note.getType() != NoteType.PDF) {
            throw new AccessDeniedException("Comments only allowed for PDF notes");
        }
        return note;
    }

    private PdfCommentDto mapToDto(PdfComment comment) {
        PdfCommentDto dto = new PdfCommentDto();
        dto.setCommentId(comment.getCommentId());
        dto.setX(comment.getX());
        dto.setY(comment.getY());
        dto.setWidth(comment.getWidth());
        dto.setHeight(comment.getHeight());
        dto.setPageNumber(comment.getPageNumber());
        dto.setComment(comment.getComment());
        dto.setUsername(comment.getUsername());
        return dto;
    }
}
