package com.olifarhaan.response;

import com.olifarhaan.model.Comment;
import com.olifarhaan.model.Issue;
import com.olifarhaan.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponseForUser {
    private Long id;
    private String content;
    private User author;
    private Issue issue;
    private LocalDateTime createdAt;

    public static CommentResponseForUser from(Comment comment) {
        return CommentResponseForUser.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor())
                .issue(comment.getIssue())
                .createdAt(comment.getCreatedAt())
                .build();
    }
} 