package com.olifarhaan.response;

import com.olifarhaan.model.Bookmark;
import com.olifarhaan.model.Issue;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookmarkResponse {
    private Long id;
    private Issue issue;
    private LocalDateTime createdAt;

    public static BookmarkResponse from(Bookmark bookmark) {
        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .issue(bookmark.getIssue())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
} 