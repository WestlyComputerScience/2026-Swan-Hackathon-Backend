package com.chat.aj.unote.Notes.Services;

import com.chat.aj.unote.Accounts.Entity.Accounts;
import com.chat.aj.unote.Accounts.repository.AccountsRepository;
import com.chat.aj.unote.Exceptions.BadRequestException;
import com.chat.aj.unote.Exceptions.ForbiddenException;
import com.chat.aj.unote.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Notes.DTO.LinkCreationDTO;
import com.chat.aj.unote.Notes.Entity.Notes;
import com.chat.aj.unote.Notes.Entity.Unit;
import com.chat.aj.unote.Notes.NoteType;
import com.chat.aj.unote.Notes.Repository.NotesRepository;
import com.chat.aj.unote.Notes.Repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkService {

    private final NotesRepository noteRepository;
    private final UnitRepository unitRepository;
    private final AccountsRepository accountsRepository;

    @Autowired
    public LinkService(NotesRepository noteRepository,
                       UnitRepository unitRepository,
                       AccountsRepository accountsRepository) {
        this.noteRepository = noteRepository;
        this.unitRepository = unitRepository;
        this.accountsRepository = accountsRepository;
    }

    public Long createLink(LinkCreationDTO dto, String username) {
        if (dto.getType() != NoteType.YT && dto.getType() != NoteType.WEBSITE) {
            throw new BadRequestException("Type must be YT or WEBSITE for link notes");
        }

        if (dto.getUrl() == null || dto.getUrl().isBlank()) {
            throw new BadRequestException("URL cannot be empty");
        }

        Unit unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit", dto.getUnitId()));

        Accounts account = accountsRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        NoteType resolvedType = dto.getType() != null
                ? dto.getType()
                : detectType(dto.getUrl());

        Notes note = new Notes();
        note.setTitle(dto.getTitle());
        note.setContent(dto.getUrl());
        note.setType(resolvedType);
        note.setUnit(unit);
        note.setUser(account);

        noteRepository.save(note);
        return note.getId();
    }

    public Notes getLink(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", id));
    }

    public List<Notes> getLinksByUnit(Long unitId) {
        return noteRepository.findByUnitIdAndTypeIn(
                unitId,
                List.of(NoteType.YT, NoteType.WEBSITE)
        );
    }

    public void deleteLink(Long id, String username) {
        Notes note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", id));

        if (!note.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("You do not have access to this note");
        }

        noteRepository.delete(note);
    }

    private NoteType detectType(String url) {
        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            return NoteType.YT;
        }
        return NoteType.WEBSITE;
    }
}
