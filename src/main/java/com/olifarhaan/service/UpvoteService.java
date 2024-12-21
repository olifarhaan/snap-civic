package com.olifarhaan.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.model.Issue;
import com.olifarhaan.model.Upvote;
import com.olifarhaan.model.User;
import com.olifarhaan.repository.UpvoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpvoteService {

    private final UpvoteRepository upvoteRepository;
    private final IssueService issueService;

    private final Logger logger = LoggerFactory.getLogger(UpvoteService.class);

    @Transactional
    public Upvote addUpvote(Long issueId, User loggedInUser) {
        logger.debug("Adding upvote for issue ID: {}", issueId);

        Issue issue = issueService.getIssueById(issueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Issue not found with ID: %d", issueId)));

        Upvote upvote = Upvote.builder()
                .user(loggedInUser)
                .issue(issue)
                .build();

        return upvoteRepository.save(upvote);
    }

    @Transactional
    public void removeUpvote(Long issueId, User loggedInUser) {
        logger.debug("Removing upvote for issue ID: {}", issueId);
        upvoteRepository.deleteByUserIdAndIssueId(loggedInUser.getId(), issueId);
    }

    @Transactional(readOnly = true)
    public List<Upvote> getUpvotesByUserId(String userId) {
        logger.debug("Fetching upvotes for user ID: {}", userId);
        return upvoteRepository.findByUserId(userId);
    }

    public Optional<Upvote> getUpvoteById(Long upvoteId) {
        logger.debug("Fetching upvote by ID: {}", upvoteId);
        return upvoteRepository.findById(upvoteId);
    }
}