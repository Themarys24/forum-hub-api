package com.forumhub.repository;

import com.forumhub.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    //Business rule: Check for duplicates (same name and title)
    boolean existsByTitleAndMessage(String title, String message);

    //Search by ID with join fetch to avoid N+1
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course WHERE t.id = :id")
    Optional<Topic> findByIdWithDetails(@Param("id") Long id);

    //LISTING: All topics sorted by creation date (ASC)
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course ORDER BY t.createdAt ASC")
    List<Topic> findAllOrderByCreatedAtAsc();

    //LISTING: 10 first topics by creation date
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course ORDER BY t.createdAt ASC")
    List<Topic> findTop10ByOrderByCreatedAtAsc();


    //PAGINATION: All topics with pagination
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course")
    Page<Topic> findAllWithDetails(Pageable pageable);

    //SEARCH BY COURSE: Topics of an especific course
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course WHERE t.course.id = :courseId ORDER BY t.createdAt ASC")
    List<Topic> findByCourseIdOrderByCreatedAtAsc(@Param("courseId") Long courseId);

    //SEARCH BY COURSE AND "AND": Suggested search criteria
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course WHERE t.course.name LIKE %:courseName% AND YEAR(t.createdAt) = :year ORDER BY t.createdAt ASC")
    List<Topic> findByCourseNameAndYear(@Param("courseName") String courseName, @Param("year") Integer year);

    //Search by status
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course WHERE t.status = :status ORDER BY t.createdAt ASC")
    List<Topic> findByStatusOrderByCreatedAtAsc(@Param("status") Topic.TopicStatus status);

    //Search by author
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course WHERE t.author.id = :authorId ORDER BY t.createdAt DESC")
    List<Topic> findByAuthorIdOrderByCreatedAtDesc(@Param("authorId") Long authorId);

    //Search by title (case insentive)
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY t.createdAt ASC")
    List<Topic> findByTitleContainingIgnoreCase(@Param("title") String title);

    //Search by period
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course WHERE t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt ASC")
    List<Topic> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    //STATICS: Count topics by status
    @Query("SELECT t.status, COUNT(t) FROM Topic t GROUP BY t.status")
    List<Object[]> countTopicsByStatus();

    //STATISTICS: Most popular topics (with most replies)
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course LEFT JOIN t.responses r GROUP BY t ORDER BY COUNT(r) DESC")
    List<Topic> findMostPopularTopics(Pageable pageable);

    //ADVANCED SEARCH: Multiple criteria
    @Query("SELECT t FROM Topic t JOIN FETCH t.author JOIN FETCH t.course WHERE " +
            "(:courseId IS NULL OR t.course.id = :courseId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:authorId IS NULL OR t.author.id = :authorId) AND " +
            "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "ORDER BY t.createdAt ASC")
    List<Topic> findByMultipleCriteria(
            @Param("courseId") Long courseId,
            @Param("status") Topic.TopicStatus status,
            @Param("authorId") Long authorId,
            @Param("title") String title
    );


}
