package com.chat.aj.unote.Notes.Services;

import com.chat.aj.unote.Accounts.Entity.Accounts;
import com.chat.aj.unote.Accounts.Service.AccountsService;
import com.chat.aj.unote.Accounts.repository.AccountsRepository;
import com.chat.aj.unote.Config.R2Service;
import com.chat.aj.unote.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Exceptions.UnauthorizedException;
import com.chat.aj.unote.Notes.DTO.NoteRequestDTO;
import com.chat.aj.unote.Notes.DTO.TextEntryDTO;
import com.chat.aj.unote.Notes.Entity.Notes;
import com.chat.aj.unote.Notes.Entity.Unit;
import com.chat.aj.unote.Notes.NoteType;
import com.chat.aj.unote.Notes.Repository.NotesRepository;
import com.chat.aj.unote.Notes.Repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class NoteService {

    private final NotesRepository noteRepository;
    private final UnitRepository unitRepository;
    private final AccountsRepository accountsRepository;
    private final R2Service r2Service;
    private final ClassesService classesService;

    @Autowired
    public NoteService(NotesRepository noteRepository, UnitRepository unitRepository, AccountsRepository accountsRepository, R2Service r2Service, ClassesService classesService) {
        this.noteRepository = noteRepository;
        this.unitRepository = unitRepository;
        this.accountsRepository = accountsRepository;
        this.r2Service = r2Service;
        this.classesService = classesService;
    }

    public Long createNote(String title, String fileUrl, Long unitId, String name, MultipartFile file) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", unitId));

        Accounts account = accountsRepository.findByUsername(name)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Notes note = new Notes();
        note.setTitle(title);
        note.setFileUrl(fileUrl);
        note.setType(NoteType.PDF);
        note.setUnit(unit);
        note.setUser(account);
        note.setFileName(file.getOriginalFilename());
        note.setFileType(file.getContentType());
        note.setFileSize(file.getSize());
        note.setType(inferType(file.getContentType()));
        noteRepository.save(note);
        return note.getId();
    }

    private NoteType inferType(String contentType) {
        if (contentType == null) return NoteType.TXTFILE;

        return switch (contentType) {
            case "application/pdf" -> NoteType.PDF;
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                 "application/vnd.ms-powerpoint" -> NoteType.PTX;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                 "application/msword" -> NoteType.DOCX;
            case "text/plain" -> NoteType.TXTFILE;
            case "image/jpeg", "image/png", "image/gif", "image/webp" -> NoteType.IMAGE;
            case "video/mp4", "video/mpeg", "video/quicktime", "video/webm" -> NoteType.VIDEO;
            default -> NoteType.TXTFILE;
        };
    }

    public void deleteNote(Long noteId, String name) {
        Optional<Notes> note = noteRepository.findById(noteId);
        if(note.isEmpty()) throw new ResourceNotFoundException("Note not found");
        Optional<Accounts> a = accountsRepository.findByUsername(name);
        if(a.isEmpty()) throw new ResourceNotFoundException("Account not found");
        if(!note.get().getUser().equals(a.get())) throw new UnauthorizedException("You do not own this note!");

        r2Service.deleteFile(note.get().getFileUrl());

        noteRepository.delete(note.get());
    }

    public List<Notes> getSpecificNotes(NoteRequestDTO noteReq) {
        Optional<Unit> u = unitRepository.findByNameAndMyClass(noteReq.getUnitName(), classesService.findClass(noteReq.getClassName()));
        if (u.isEmpty()) throw new ResourceNotFoundException("Unit or Class not found");
        return noteRepository.findByUnit(u.get());
    }

    public Long createTextNote(TextEntryDTO textDTO, String name) {
        Optional<Accounts> a = accountsRepository.findByUsername(name);
        if (a.isEmpty()) throw new ResourceNotFoundException("Account Not Found");
        Optional<Unit> u = unitRepository.findById(textDTO.getUnitId());
        if (u.isEmpty()) throw new ResourceNotFoundException("Account Not Found");
        Notes n = new Notes();
        n.setCreatedAt(LocalDateTime.now());
        n.setType(NoteType.TEXT);
        n.setContent(textDTO.getContent());
        n.setUser(a.get());
        n.setUnit(u.get());
        n.setTitle(textDTO.getTitle());
        noteRepository.save(n);
        return n.getId();
    }

    public List<Notes> getALl() {
        return noteRepository.findAll();
    }
}
