package com.olifarhaan.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.Upvote;
import com.olifarhaan.service.UpvoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/upvotes")
@RequiredArgsConstructor
@Tag(name = "Upvote Controller", description = "APIs for managing upvotes")
public class UpvoteController extends BaseController {

    private final UpvoteService upvoteService;

    @PostMapping("/issues/{issueId}")
    @Operation(summary = "Add an upvote")
    public ResponseEntity<Void> addUpvote(@PathVariable Long issueId) {
        upvoteService.addUpvote(issueId, getLoggedInUser());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/issues/{issueId}")
    @Operation(summary = "Remove an upvote")
    public ResponseEntity<Void> removeUpvote(@PathVariable Long issueId) {
        upvoteService.removeUpvote(issueId, getLoggedInUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get upvotes by user ID")
    public ResponseEntity<List<Upvote>> getUpvotesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(upvoteService.getUpvotesByUserId(userId));
    }
}