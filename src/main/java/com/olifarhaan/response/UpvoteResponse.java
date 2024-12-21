package com.olifarhaan.response;

import com.olifarhaan.model.Issue;
import com.olifarhaan.model.Upvote;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpvoteResponse {
    private Long id;
    private Issue issue;
    private LocalDateTime createdAt;

    public static UpvoteResponse from(Upvote upvote) {
        return UpvoteResponse.builder()
                .id(upvote.getId())
                .issue(upvote.getIssue())
                .createdAt(upvote.getCreatedAt())
                .build();
    }
} 