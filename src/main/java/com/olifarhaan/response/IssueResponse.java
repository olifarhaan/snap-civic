package com.olifarhaan.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.olifarhaan.model.Address;
import com.olifarhaan.model.Issue.IssueCategory;
import com.olifarhaan.model.Issue.IssueStatus;
import com.olifarhaan.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssueResponse {
    private Long id;
    private String title;
    private String description;
    private Set<IssueCategory> categories;
    private List<String> images;
    private IssueStatus status;
    private Address address;
    private User reportedBy;
    private LocalDateTime createdAt;
    private int upvotes;
    private int comments;
    private int bookmarks;
    private double distance;
}