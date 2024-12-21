package com.olifarhaan.service;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olifarhaan.model.Issue;
import com.olifarhaan.model.Issue.IssueStats;
import com.olifarhaan.response.IssueResponse;

import lombok.Getter;

@Service
@Getter
public class ResponseTransformerService {

    @Autowired
    private UpvoteService upvoteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private IssueService issueService;

    private Function<Issue, IssueResponse> issueToIssueResponse = issue -> {
        IssueStats stats = issueService.getIssueStats(issue.getId());
        double distance = Math.round(issue.getLocation().distance(issue.getReportedBy().getLocation()) * 1000.0 * 100.0)
                / 100.0;
        return IssueResponse.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .categories(issue.getCategories())
                .images(issue.getImages())
                .status(issue.getStatus())
                .address(issue.getAddress())
                .reportedBy(issue.getReportedBy())
                .createdAt(issue.getCreatedAt())
                .upvotes(stats.getUpvotes())
                .comments(stats.getComments())
                .bookmarks(stats.getBookmarks())
                .distance(distance)
                .build();
    };
}
