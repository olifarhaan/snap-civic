package com.olifarhaan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.olifarhaan.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            SELECT new com.olifarhaan.model.Comment$LightComment(c.id, c.content, c.author, c.createdAt)
            FROM Comment c
            WHERE c.issue.id = :issueId
            ORDER BY c.createdAt DESC
            """)
    List<Comment.LightComment> findByIssueId(Long issueId);

    @Query("""
            SELECT c FROM Comment c
            LEFT JOIN FETCH c.issue
            WHERE c.author.id = :userId
            ORDER BY c.createdAt DESC
            """)
    List<Comment> findByAuthorId(String userId);

}