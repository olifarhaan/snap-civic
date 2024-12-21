package com.olifarhaan.model;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "issues")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Issue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotEmpty
    private Set<IssueCategory> categories;

    @NotEmpty
    private List<String> images;

    @NotNull
    private IssueStatus status;

    @NotNull
    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @NotNull
    private Point location;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User reportedBy;

    public enum IssueStatus {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }

    public enum IssueCategory {
        INFRASTRUCTURE,
        PUBLIC_SAFETY,
        SANITATION_AND_CLEANLINESS,
        ENVIRONMENT,
        PUBLIC_UTILITIES,
        COMMUNITY_SERVICES,
        NOISE_AND_NUISANCE,
        ACCESSIBILITY,
        CULTURE_AND_HERITAGE,
        OTHER
    }

    public enum SortBy {
        NEAREST,
        TRENDING,
        RECENT
    }

    public interface IssueStats {
        int getUpvotes();

        int getComments();

        int getBookmarks();
    }
}
