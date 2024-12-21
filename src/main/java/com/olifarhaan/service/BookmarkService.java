package com.olifarhaan.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.model.Bookmark;
import com.olifarhaan.model.Issue;
import com.olifarhaan.model.User;
import com.olifarhaan.repository.BookmarkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final IssueService issueService;

    private final Logger logger = LoggerFactory.getLogger(BookmarkService.class);

    @Transactional
    public Bookmark addBookmark(Long issueId, User loggedInUser) {
        logger.debug("Adding bookmark for issue ID: {}", issueId);

        Issue issue = issueService.getIssueById(issueId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Issue not found with ID: %d", issueId)));

        Bookmark bookmark = Bookmark.builder()
                .user(loggedInUser)
                .issue(issue)
                .build();

        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long issueId, User loggedInUser) {
        logger.debug("Removing bookmark for issue ID: {}", issueId);
        bookmarkRepository.deleteByUserIdAndIssueId(loggedInUser.getId(), issueId);
    }

    @Transactional(readOnly = true)
    public List<Bookmark> getBookmarksByUserId(String userId) {
        logger.debug("Fetching bookmarks for user ID: {}", userId);
        return bookmarkRepository.findByUserId(userId);
    }
}