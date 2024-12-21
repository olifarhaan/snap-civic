package com.olifarhaan.repository;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.olifarhaan.model.Issue;
import com.olifarhaan.model.Issue.IssueCategory;
import com.olifarhaan.model.Issue.IssueStats;
import com.olifarhaan.model.Issue.IssueStatus;
import com.olifarhaan.model.Issue.SortBy;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query(value = """
            SELECT i.*
            FROM issues i
            WHERE ST_Distance_Sphere(i.location, :point) <= :distance
            AND (:status IS NULL OR i.status = :status)
            AND (:categories IS NULL OR i.categories && :categories)
            ORDER BY
            CASE
                WHEN :sortBy = 'NEAREST' THEN ST_Distance_Sphere(i.location, :point)
                WHEN :sortBy = 'TRENDING' THEN i.created_at
                ELSE i.created_at
            END DESC
            """, nativeQuery = true)
    List<Issue> findNearbyIssues(
            @Param("point") Point point,
            @Param("distance") double distance,
            @Param("status") IssueStatus status,
            @Param("categories") IssueCategory category,
            @Param("sortBy") SortBy sortBy);

    @Query(value = """
            SELECT
                COUNT(DISTINCT u.id) as upvotes,
                COUNT(DISTINCT c.id) as comments,
                COUNT(DISTINCT b.id) as bookmarks
            FROM issues i
            LEFT JOIN upvotes u ON i.id = u.issue_id
            LEFT JOIN comments c ON i.id = c.issue_id
            LEFT JOIN bookmarks b ON i.id = b.issue_id
            WHERE i.id = :issueId
            """, nativeQuery = true)
    IssueStats getIssueCounts(@Param("issueId") Long issueId);
}