package com.forumhub.repository;

import com.forumhub.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    // Search for answers to a specific topic
    @Query("SELECT r FROM Response r JOIN FETCH r.author WHERE r.topic.id = :topicId ORDER BY r.createdAt ASC")
    List<Response> findByTopicIdOrderByCreatedAtAsc(@Param("topicId") Long topicId);

    // Search for an answer with details (author and topic)
    @Query("SELECT r FROM Response r JOIN FETCH r.author JOIN FETCH r.topic WHERE r.id = :id")
    Optional<Response> findByIdWithDetails(@Param("id") Long id);

    // Search for answers from a specific author
    @Query("SELECT r FROM Response r JOIN FETCH r.topic WHERE r.author.id = :authorId ORDER BY r.createdAt DESC")
    List<Response> findByAuthorIdOrderByCreatedAtDesc(@Param("authorId") Long authorId);

    // Search for an answer marked as a solution in a topic
    @Query("SELECT r FROM Response r JOIN FETCH r.author WHERE r.topic.id = :topicId AND r.solution = true")
    Optional<Response> findSolutionByTopicId(@Param("topicId") Long topicId);

    // Search all answers marked as solution
    @Query("SELECT r FROM Response r JOIN FETCH r.author JOIN FETCH r.topic WHERE r.solution = true ORDER BY r.createdAt DESC")
    List<Response> findAllSolutions();

    // Count replies in a topic
    long countByTopicId(Long topicId);

    // Count responses from an author
    long countByAuthorId(Long authorId);

    // Check if a user has already replied to a topic
    boolean existsByTopicIdAndAuthorId(Long topicId, Long authorId);

    // Search for a user's latest responses
    @Query("SELECT r FROM Response r JOIN FETCH r.topic WHERE r.author.id = :authorId ORDER BY r.createdAt DESC")
    List<Response> findLatestResponsesByAuthor(@Param("authorId") Long authorId, org.springframework.data.domain.Pageable pageable);


}
