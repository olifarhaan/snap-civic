package com.olifarhaan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.Comment;
import com.olifarhaan.model.Comment.LightComment;
import com.olifarhaan.request.CommentRequest;
import com.olifarhaan.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "APIs for managing comments")
public class CommentController extends BaseController {

    private final CommentService commentService;

    @PostMapping("/issues/{issueId}")
    @Operation(summary = "Create a new comment")
    public ResponseEntity<LightComment> createComment(
            @PathVariable Long issueId,
            @Valid @RequestBody CommentRequest request) {
        LightComment comment = commentService.createComment(issueId, request, getLoggedInUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Update an existing comment")
    public ResponseEntity<LightComment> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request) {
        LightComment comment = commentService.updateComment(commentId, request, getLoggedInUser());
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete a comment")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId, getLoggedInUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/issues/{issueId}")
    @Operation(summary = "Get comments by issue ID")
    public ResponseEntity<List<LightComment>> getCommentsByIssueId(@PathVariable Long issueId) {
        return ResponseEntity.ok(commentService.getCommentsByIssueId(issueId));
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get comments by user ID")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(userId));
    }
}