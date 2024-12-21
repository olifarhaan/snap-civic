package com.olifarhaan.repository;

import com.olifarhaan.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    
    @Query("SELECT b FROM Bookmark b LEFT JOIN FETCH b.issue WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    List<Bookmark> findByUserId(String userId);
    
    Optional<Bookmark> findByUserIdAndIssueId(String userId, Long issueId);
    
    void deleteByUserIdAndIssueId(String userId, Long issueId);

} 