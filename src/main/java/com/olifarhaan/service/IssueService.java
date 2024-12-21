package com.olifarhaan.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.olifarhaan.model.Address;
import com.olifarhaan.model.Issue;
import com.olifarhaan.model.Issue.IssueCategory;
import com.olifarhaan.model.Issue.IssueStats;
import com.olifarhaan.model.Issue.IssueStatus;
import com.olifarhaan.model.Issue.SortBy;
import com.olifarhaan.model.User;
import com.olifarhaan.repository.IssueRepository;
import com.olifarhaan.request.IssueRequest;
import com.olifarhaan.util.GeometryUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final GeometryUtil geometryUtil;

    private final Logger logger = LoggerFactory.getLogger(IssueService.class);

    @Transactional
    public Issue createIssue(IssueRequest request, User loggedInUser) {
        logger.debug("Creating new issue with title: {}", request.getTitle());

        Point location = geometryUtil.createPoint(request.getAddress().getLatitude(),
                request.getAddress().getLongitude());

        Issue issue = Issue.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .categories(request.getCategories())
                .images(request.getImages())
                .status(request.getStatus())
                .address(Address.builder()
                        .completeAddress(request.getAddress().getCompleteAddress())
                        .latitude(request.getAddress().getLatitude())
                        .longitude(request.getAddress().getLongitude())
                        .build())
                .location(location)
                .reportedBy(loggedInUser)
                .build();

        return issueRepository.save(issue);
    }

    @Transactional(readOnly = true)
    public Optional<Issue> getIssueById(Long issueId) {
        logger.debug("Fetching issue with ID: {}", issueId);

        return issueRepository.findById(issueId);
    }

    @Transactional(readOnly = true)
    public Issue getIssue(Long issueId) {
        return getIssueById(issueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Issue not found with ID: %d", issueId)));
    }

    @Transactional
    public Issue updateIssue(Long issueId, IssueRequest request, User loggedInUser) {
        logger.debug("Updating issue with ID: {}", issueId);

        Issue issue = getIssueById(issueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Issue not found with ID: %d", issueId)));

        if (!issue.getReportedBy().getId().equals(loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this issue");
        }

        if (StringUtils.isNotEmpty(request.getTitle())) {
            issue.setTitle(request.getTitle());
        }
        if (StringUtils.isNotEmpty(request.getDescription())) {
            issue.setDescription(request.getDescription());
        }
        if (ObjectUtils.isNotEmpty(request.getCategories())) {
            issue.setCategories(request.getCategories());
        }
        if (ObjectUtils.isNotEmpty(request.getImages())) {
            issue.setImages(request.getImages());
        }
        if (request.getStatus() != null) {
            issue.setStatus(request.getStatus());
        }
        if (ObjectUtils.isNotEmpty(request.getAddress())) {
            issue.setAddress(Address.builder()
                    .id(issue.getAddress().getId())
                    .completeAddress(request.getAddress().getCompleteAddress())
                    .latitude(request.getAddress().getLatitude())
                    .longitude(request.getAddress().getLongitude())
                    .build());
            Point location = geometryUtil.createPoint(request.getAddress().getLatitude(),
                    request.getAddress().getLongitude());
            issue.setLocation(location);
        }

        return issueRepository.save(issue);
    }

    @Transactional
    public void deleteIssue(Long issueId, User loggedInUser) {
        logger.debug("Deleting issue with ID: {}", issueId);

        Issue issue = getIssueById(issueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Issue not found with ID: %d", issueId)));

        if (!issue.getReportedBy().getId().equals(loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this issue");
        }

        issueRepository.delete(issue);
    }

    @Transactional(readOnly = true)
    public List<Issue> getFeed(double distance, double latitude, double longitude,
            IssueCategory category, IssueStatus status,
            SortBy sortBy) {
        logger.debug(
                "Fetching feed with parameters - distance: {}, lat: {}, lon: {}, category: {}, status: {}, sortBy: {}",
                distance, latitude, longitude, category, status, sortBy);

        Point point = geometryUtil.createPoint(latitude, longitude);

        return issueRepository.findNearbyIssues(point, distance, status, category, sortBy);
    }

    @Transactional(readOnly = true)
    public IssueStats getIssueStats(Long issueId) {
        logger.debug("Fetching issue stats for issue ID: {}", issueId);
        return issueRepository.getIssueCounts(issueId);
    }
}