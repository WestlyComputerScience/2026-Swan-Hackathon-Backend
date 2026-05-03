package com.chat.aj.unote.Notes.Controllers;

import com.chat.aj.unote.Accounts.security.accounts.AccountsDetails;
import com.chat.aj.unote.Notes.DTO.LinkCreationDTO;
import com.chat.aj.unote.Notes.Entity.Notes;
import com.chat.aj.unote.Notes.Services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/v1/links")
public class LinkController {

    private final LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public ResponseEntity<?> createLink(
            @RequestBody LinkCreationDTO dto,
            @AuthenticationPrincipal AccountsDetails userDetails) {
        Long id = linkService.createLink(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLink(@PathVariable Long id) {
        Notes note = linkService.getLink(id);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/unit/{unitId}")
    public ResponseEntity<?> getLinksByUnit(@PathVariable Long unitId) {
        List<Notes> links = linkService.getLinksByUnit(unitId);
        return ResponseEntity.ok(links);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLink(
            @PathVariable Long id,
            @AuthenticationPrincipal AccountsDetails userDetails) {
        linkService.deleteLink(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Link deleted successfully"));
    }
}
