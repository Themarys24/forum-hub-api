package com.forumhub.repository;

import com.forumhub.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    //Search active courses
    List<Course> findByActiveTrue();

    //Search active course by ID
    Optional<Course> findByIdAndActiveTrue(Long id);


    @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND c.active = true")
    List<Course> findByNameContainingIgnoreCaseAndActiveTrue(@Param("name") String name);


    //Search by category
    List<Course> findByCategoryAndActiveTrue(String category);

    //Search by category (case insensitive)
    @Query("SELECT c FROM Course c WHERE LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')) AND c.active = true")
    List<Course> findByCategoryContainingIgnoreCaseAndActiveTrue(@Param("category") String category);


    //Check if name already exists
    boolean existsByNameAndActiveTrue(String name);


    //Search all distincts categories
    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.active = true AND c.category IS NOT NULL ORDER BY c.category")
    List<String> findDistinctCategories();


    //Count courses by category
    @Query("SELECT c.category, COUNT(c) FROM Course c WHERE c.active = true GROUP BY c.category")
    List<Object[]> countCoursesByCategory();



}
