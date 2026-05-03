package com.chat.aj.unote.Notes.Controllers;

import com.chat.aj.unote.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Accounts.response.ApiResponse;
import com.chat.aj.unote.Notes.Services.VideoTimestampService;
import com.chat.aj.unote.Notes.request.CreateTimestampRequest;
import com.chat.aj.unote.Notes.request.UpdateTimestampRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/notes/{notesId}/timestamps")
@RequiredArgsConstructor
public class VideoTimestampController {

    private final VideoTimestampService timestampService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addTimestamp(@PathVariable Long notesId, @RequestBody CreateTimestampRequest request,
                                                    Principal principal) {
        try {
            String username = principal.getName();

            return ResponseEntity.ok(new ApiResponse("Create timestamp success!",
                            timestampService.createVideoTimestamp(notesId, request, username)));
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
            return ResponseEntity.ok(
                    new ApiResponse("Success", timestampService.getAllVideoTimestamps(notesId)));
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
                                              @RequestBody UpdateTimestampRequest request, Principal principal) {
        try {
            String username = principal.getName();

            return ResponseEntity.ok(
                    new ApiResponse("Updated", timestampService.updateVideoTimestamp(notesId, commentId, request, username)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long notesId, @PathVariable Long commentId, Principal principal) {
        try {
            String username = principal.getName();
            timestampService.deleteVideoTimestamp(notesId, commentId, username);
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