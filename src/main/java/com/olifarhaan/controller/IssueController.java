package com.olifarhaan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.Issue;
import com.olifarhaan.model.Issue.IssueCategory;
import com.olifarhaan.model.Issue.IssueStatus;
import com.olifarhaan.model.Issue.SortBy;
import com.olifarhaan.request.IssueRequest;
import com.olifarhaan.response.IssueResponse;
import com.olifarhaan.service.IssueService;
import com.olifarhaan.service.ResponseTransformerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
@Tag(name = "Issue Controller", description = "APIs for managing issues")
public class IssueController extends BaseController {

    private final IssueService issueService;
    private final ResponseTransformerService responseTransformerService;

    @PostMapping
    @Operation(summary = "Create a new issue")
    public ResponseEntity<IssueResponse> createIssue(@Valid @RequestBody IssueRequest request) {
        Issue issue = issueService.createIssue(request, getLoggedInUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseTransformerService.getIssueToIssueResponse().apply(issue));
    }

    @GetMapping("/{issueId}")
    @Operation(summary = "Get issue by ID")
    public ResponseEntity<IssueResponse> getIssueById(@PathVariable Long issueId) {
        return ResponseEntity
                .ok(responseTransformerService.getIssueToIssueResponse().apply(issueService.getIssue(issueId)));
    }

    @PutMapping("/{issueId}")
    @Operation(summary = "Update an existing issue")
    public ResponseEntity<IssueResponse> updateIssue(
            @PathVariable Long issueId,
            @Valid @RequestBody IssueRequest request) {
        Issue issue = issueService.updateIssue(issueId, request, getLoggedInUser());
        return ResponseEntity.ok(responseTransformerService.getIssueToIssueResponse().apply(issue));
    }

    @DeleteMapping("/{issueId}")
    @Operation(summary = "Delete an issue")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId, getLoggedInUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/feed")
    @Operation(summary = "Get issue feed based on location and filters")
    public ResponseEntity<List<IssueResponse>> getFeed(
            @RequestParam double distance,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) IssueCategory category,
            @RequestParam(required = false, defaultValue = "OPEN,IN_PROGRESS") IssueStatus status,
            @RequestParam(required = false, defaultValue = "NEAREST") SortBy sortBy) {
        List<Issue> issues = issueService.getFeed(distance, latitude, longitude, category, status, sortBy);
        return ResponseEntity.ok(issues.stream()
                .map(responseTransformerService.getIssueToIssueResponse())
                .toList());
    }
}