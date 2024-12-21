package com.olifarhaan.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.model.Comment;
import com.olifarhaan.model.Comment.LightComment;
import com.olifarhaan.model.Issue;
import com.olifarhaan.model.User;
import com.olifarhaan.repository.CommentRepository;
import com.olifarhaan.repository.IssueRepository;
import com.olifarhaan.request.CommentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;

    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Transactional
    public LightComment createComment(Long issueId, CommentRequest request, User loggedInUser) {
        logger.debug("Creating comment for issue ID: {}", issueId);

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Issue not found with ID: %d", issueId)));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(loggedInUser)
                .issue(issue)
                .build();

        return new LightComment(commentRepository.save(comment));
    }

    @Transactional
    public LightComment updateComment(Long commentId, CommentRequest request, User loggedInUser) {
        logger.debug("Updating comment with ID: {}", commentId);

        Comment comment = getCommentById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Comment not found with ID: %d", commentId)));

        if (!comment.getAuthor().getId().equals(loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this comment");
        }

        if (StringUtils.isNotEmpty(request.getContent())) {
            comment.setContent(request.getContent());
        }
        return new LightComment(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long commentId) {
        logger.debug("Fetching comment by ID: {}", commentId);
        return commentRepository.findById(commentId);
    }

    @Transactional
    public void deleteComment(Long commentId, User loggedInUser) {
        logger.debug("Deleting comment with ID: {}", commentId);

        Comment comment = getCommentById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Comment not found with ID: %d", commentId)));

        if (!comment.getAuthor().getId().equals(loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<LightComment> getCommentsByIssueId(Long issueId) {
        logger.debug("Fetching comments for issue ID: {}", issueId);
        return commentRepository.findByIssueId(issueId);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByUserId(String userId) {
        logger.debug("Fetching comments for user ID: {}", userId);
        return commentRepository.findByAuthorId(userId);
    }
}