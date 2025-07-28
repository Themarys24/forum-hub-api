package com.forumhub.service;

import com.forumhub.dto.request.CourseRequest;
import com.forumhub.dto.response.CourseResponse;
import com.forumhub.entity.Course;
import com.forumhub.exceptions.ResourceNotFoundException;
import com.forumhub.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseResponse> findAll() {
        return courseRepository.findByActiveTrue()
                .stream()
                .map(CourseResponse::new)
                .collect(Collectors.toList());
    }

    public CourseResponse findById(Long id) {
        Course course = courseRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return new CourseResponse(course);
    }

    public CourseResponse create(CourseRequest request) {
        // Check if already exists
        if (courseRepository.existsByNameAndActiveTrue(request.name())) {
            throw new RuntimeException("Course name already exists");
        }

        Course course = new Course(
                request.name(),
                request.category(),
                request.description()
        );

        Course savedCourse = courseRepository.save(course);
        return new CourseResponse(savedCourse);
    }

    public CourseResponse update(Long id, CourseRequest request) {
        Course course = courseRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        // Check if name already exists for another course
        if (!course.getName().equals(request.name()) &&
                courseRepository.existsByNameAndActiveTrue(request.name())) {
            throw new RuntimeException("Course name already exists");

    }

        course.setName(request.name());
        course.setCategory(request.category());
        course.setDescription(request.description());

        Course updatedCourse = courseRepository.save(course);
        return new CourseResponse(updatedCourse);

    }

    public void delete(Long id) {
        Course course = courseRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        // Soft delete
        course.deactivate();
        courseRepository.save(course);
    }

    public List<CourseResponse> findByCategory(String category) {
        return courseRepository.findByCategoryContainingIgnoreCaseAndActiveTrue(category)
                .stream()
                .map(CourseResponse::new)
                .collect(Collectors.toList());
    }
    public List<CourseResponse> searchByName(String name) {
        return courseRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(CourseResponse::new)
                .collect(Collectors.toList());
    }

    public List<String> findAllCategories() {
        return courseRepository.findDistinctCategories();
    }

}
