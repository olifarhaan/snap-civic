package com.olifarhaan.request;

import java.util.List;
import java.util.Set;

import com.olifarhaan.model.Issue.IssueCategory;
import com.olifarhaan.model.Issue.IssueStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssueRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotEmpty(message = "At least one category is required")
    private Set<IssueCategory> categories;

    @NotEmpty(message = "At least one image is required")
    private List<String> images;

    @NotNull(message = "Status is required")
    private IssueStatus status;

    @Valid
    @NotNull(message = "Address is required")
    private AddressRequest address;
}