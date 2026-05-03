package com.chat.aj.unote.Notes.Controllers;

import com.chat.aj.unote.Config.R2Service;
import com.chat.aj.unote.Notes.DTO.NoteRequestDTO;
import com.chat.aj.unote.Notes.DTO.TextEntryDTO;
import com.chat.aj.unote.Notes.Entity.Notes;
import com.chat.aj.unote.Notes.Repository.NotesRepository;
import com.chat.aj.unote.Notes.Services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/v1/notes")
public class NoteController {
    private final NoteService noteService;
    private final R2Service r2Service;

    @Autowired
    public NoteController(NoteService noteService, R2Service r2Service) {
        this.noteService = noteService;
        this.r2Service = r2Service;
    }

    @GetMapping
    public ResponseEntity<List<Notes>> allNotes(@RequestBody NoteRequestDTO noteReq){
        List<Notes> n = noteService.getSpecificNotes(noteReq);
        return ResponseEntity.ok(n);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createNote(
            @RequestParam("file") MultipartFile file,
            @RequestParam("unitId") Long unitId,
            @RequestParam("title") String title,
            Principal principal) throws IOException {  // ADD THIS

        String fileUrl = r2Service.uploadFile(file);
        Long id = noteService.createNote(title, fileUrl, unitId, principal.getName(), file);
        return ResponseEntity.ok(Map.of("id", id, "fileUrl", fileUrl));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Notes>> getAllNotes(){
        return ResponseEntity.ok(noteService.getALl());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNote(@RequestParam("id") Long noteId, Principal principal){
        noteService.deleteNote(noteId, principal.getName());
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/textentry")
    public ResponseEntity<?> textField(@RequestBody TextEntryDTO textDTO, Principal principal){
        Long id = noteService.createTextNote(textDTO, principal.getName());
        return ResponseEntity.ok(id);
    }
}
