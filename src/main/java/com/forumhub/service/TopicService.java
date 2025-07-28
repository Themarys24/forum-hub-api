package com.forumhub.service;

import com.forumhub.dto.request.TopicRequest;
import com.forumhub.dto.request.TopicUpdateRequest;
import com.forumhub.dto.response.TopicListResponse;
import com.forumhub.dto.response.TopicResponse;
import com.forumhub.entity.Course;
import com.forumhub.entity.Topic;
import com.forumhub.entity.User;
import com.forumhub.exceptions.ResourceNotFoundException;
import com.forumhub.repository.CourseRepository;
import com.forumhub.repository.TopicRepository;
import com.forumhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    // LIST ALL TOPICS (Sorted by creation date ASC)
    public List<TopicListResponse> findAll() {
        return topicRepository.findAllOrderByCreatedAtAsc()
                .stream()
                .map(TopicListResponse::new)
                .collect(Collectors.toList());
    }

    // LIST THE FIRST 10 TOPICS
    public List<TopicListResponse> findTop10() {
        return topicRepository.findTop10ByOrderByCreatedAtAsc()
                .stream()
                .map(TopicListResponse::new)
                .collect(Collectors.toList());
    }

    // LIST WITH PAGINATION
    public Page<TopicListResponse> findAllPaginated(Pageable pageable) {
        Page<Topic> topics = topicRepository.findAllWithDetails(pageable);
        return topics.map(TopicListResponse::new);
    }

    // Search especific topic by ID
    public TopicResponse findById(Long id) {
        Topic topic = topicRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
        return new TopicResponse(topic);
    }

    // Create new topic
    @Transactional
    public TopicResponse create(TopicRequest request) {
        // BUSINESS RULE: Check if there is no topic with the same title and message
        if (topicRepository.existsByTitleAndMessage(request.title(), request.message())) {
            throw new RuntimeException("Topic with same title and message already exists");
        }

        // Search for authenticated user
        User author = getCurrentUser();

        // Search course
        Course course = courseRepository.findByIdAndActiveTrue(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.courseId()));

        // Create Topic
        Topic topic = new Topic(
                request.title(),
                request.message(),
                author,
                course
        );

        Topic savedTopic = topicRepository.save(topic);
        return new TopicResponse(savedTopic);
    }

    // UPDATE TOPIC
    @Transactional
    public TopicResponse update(Long id, TopicUpdateRequest request) {
        Topic topic = topicRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));

        // Check if the user is the topic author
        User currentUser = getCurrentUser();
        if (!topic.getAuthor().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You can only update your own topics");
        }

        // Check duplicate only if title or message changed
        if (request.title() != null && !request.title().equals(topic.getTitle()) ||
                request.message() != null && !request.message().equals(topic.getMessage())) {

            String newTitle = request.title() != null ? request.title() : topic.getTitle();
            String newMessage = request.message() != null ? request.message() : topic.getMessage();

            if (topicRepository.existsByTitleAndMessage(newTitle, newMessage)) {
                throw new RuntimeException("Topic with same title and message already exists");
            }
        }

        // Update fields if provided
        if (request.title() != null && !request.title().isBlank()) {
            topic.setTitle(request.title());
        }
        if (request.message() != null && !request.message().isBlank()) {
            topic.setMessage(request.message());
        }

        Topic updatedTopic = topicRepository.save(topic);
        return new TopicResponse(updatedTopic);
    }

    // DELETE TOPIC
    @Transactional
    public void delete(Long id) {
        Topic topic = topicRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));

        // Verificar se o usuário é o autor do tópico ou admin
        User currentUser = getCurrentUser();
        if (!topic.getAuthor().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You can only delete your own topics");
        }

        topicRepository.delete(topic);
    }

    // SEARCH BY COURSE AND YEAR
    public List<TopicListResponse> findByCourseAndYear(String courseName, Integer year) {
        return topicRepository.findByCourseNameAndYear(courseName, year)
                .stream()
                .map(TopicListResponse::new)
                .collect(Collectors.toList());
    }

    // SEARCH BY STATUS
    public List<TopicListResponse> findByStatus(Topic.TopicStatus status) {
        return topicRepository.findByStatusOrderByCreatedAtAsc(status)
                .stream()
                .map(TopicListResponse::new)
                .collect(Collectors.toList());
    }

    // SEARCH BY COURSE
    public List<TopicListResponse> findByCourse(Long courseId) {
        return topicRepository.findByCourseIdOrderByCreatedAtAsc(courseId)
                .stream()
                .map(TopicListResponse::new)
                .collect(Collectors.toList());
    }

    // SEARCH BY TITLE
    public List<TopicListResponse> searchByTitle(String title) {
        return topicRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(TopicListResponse::new)
                .collect(Collectors.toList());
    }

    // SEARCH TOPICS FROM CURRENT USER
    public List<TopicListResponse> findMyTopics() {
        User currentUser = getCurrentUser();
        return topicRepository.findByAuthorIdOrderByCreatedAtDesc(currentUser.getId())
                .stream()
                .map(TopicListResponse::new)
                .collect(Collectors.toList());
    }

    // CLOSE TOPIC
    @Transactional
    public TopicResponse closeTopic(Long id) {
        Topic topic = topicRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));

        // Check permission
        User currentUser = getCurrentUser();
        if (!topic.getAuthor().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().equals(User.Role.ADMIN) &&
                !currentUser.getRole().equals(User.Role.MODERATOR)) {
            throw new RuntimeException("You don't have permission to close this topic");
        }

        topic.closeTopic();
        Topic updatedTopic = topicRepository.save(topic);
        return new TopicResponse(updatedTopic);
    }

    // MARK AS RESOLVED
    @Transactional
    public TopicResponse markAsSolved(Long id) {
        Topic topic = topicRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));

        // Check permission
        User currentUser = getCurrentUser();
        if (!topic.getAuthor().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().equals(User.Role.ADMIN) &&
                !currentUser.getRole().equals(User.Role.MODERATOR)) {
            throw new RuntimeException("You don't have permission to mark this topic as solved");
        }

        topic.markAsSolved();
        Topic updatedTopic = topicRepository.save(topic);
        return new TopicResponse(updatedTopic);
    }

    // Método auxiliar para obter usuário atual
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return (User) userRepository.findByEmail(email);
    }


}
