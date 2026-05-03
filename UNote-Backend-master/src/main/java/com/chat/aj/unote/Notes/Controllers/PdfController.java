package com.chat.aj.unote.Notes.Controllers;

import com.chat.aj.unote.Accounts.response.ApiResponse;
import com.chat.aj.unote.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Notes.Services.PdfService;
import com.chat.aj.unote.Notes.request.CreatePdfCommentRequest;
import com.chat.aj.unote.Notes.request.UpdatePdfCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes/{notesId}/pdfs-comments")
@RequiredArgsConstructor
public class PdfController {
    private final PdfService pdfService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@PathVariable Long notesId, @RequestBody CreatePdfCommentRequest request,
                                              Principal principal) {
        try {
            String username = principal.getName();
            return ResponseEntity.ok(
                    new ApiResponse("Created",
                            pdfService.createPdfComment(notesId, request, username)));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse(e.getMessage(), null));

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAll(@PathVariable Long notesId) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success", pdfService.getAllPdfComments(notesId)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse(e.getMessage(), null));

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{commentId}/update")
    public ResponseEntity<ApiResponse> update(@PathVariable Long notesId, @PathVariable Long commentId,
                                              @RequestBody UpdatePdfCommentRequest request, Principal principal) {
        try {
            String username = principal.getName();
            return ResponseEntity.ok(
                    new ApiResponse("Updated",
                            pdfService.updatePdfComment(notesId, commentId, request, username))
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse(e.getMessage(), null));

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long notesId, @PathVariable Long commentId, Principal principal) {
        try {
            String username = principal.getName();
            pdfService.deletePdfComment(notesId, commentId, username);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse(e.getMessage(), null));

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
