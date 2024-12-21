package com.olifarhaan.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.Bookmark;
import com.olifarhaan.service.BookmarkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Tag(name = "Bookmark Controller", description = "APIs for managing bookmarks")
public class BookmarkController extends BaseController {

    private final BookmarkService bookmarkService;

    @PostMapping("/issues/{issueId}")
    @Operation(summary = "Add a bookmark")
    public ResponseEntity<Void> addBookmark(@PathVariable Long issueId) {
        bookmarkService.addBookmark(issueId, getLoggedInUser());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/issues/{issueId}")
    @Operation(summary = "Remove a bookmark")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long issueId) {
        bookmarkService.removeBookmark(issueId, getLoggedInUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get bookmarks by user ID")
    public ResponseEntity<List<Bookmark>> getBookmarksByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(bookmarkService.getBookmarksByUserId(userId));
    }
}