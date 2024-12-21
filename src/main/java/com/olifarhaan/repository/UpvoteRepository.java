package com.olifarhaan.repository;

import com.olifarhaan.model.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UpvoteRepository extends JpaRepository<Upvote, Long> {
    
    @Query("SELECT u FROM Upvote u LEFT JOIN FETCH u.issue WHERE u.user.id = :userId ORDER BY u.createdAt DESC")
    List<Upvote> findByUserId(String userId);
    
    Optional<Upvote> findByUserIdAndIssueId(String userId, Long issueId);
    
    void deleteByUserIdAndIssueId(String userId, Long issueId);

} 