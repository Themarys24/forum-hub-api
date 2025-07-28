package com.forumhub.controller;

import com.forumhub.dto.request.TopicRequest;
import com.forumhub.dto.request.TopicUpdateRequest;
import com.forumhub.dto.response.TopicDetailResponse;
import com.forumhub.dto.response.TopicListResponse;
import com.forumhub.dto.response.TopicResponse;
import com.forumhub.entity.Topic;
import com.forumhub.exceptions.ResourceNotFoundException;
import com.forumhub.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    // GET /topics - LIST ALL TOPICS (ordered by creation date ASC)
    @GetMapping
    public ResponseEntity<List<TopicListResponse>> getAllTopics(
            @RequestParam(required = false) String course,
            @RequestParam(required = false) Integer year) {

        List<TopicListResponse> topics;

        // SEARCH BY COURSE AND YEAR (suggested criteria in challenge)
        if (course != null && year != null) {
            topics = topicService.findByCourseAndYear(course, year);
        } else {
            topics = topicService.findAll();
        }

        return ResponseEntity.ok(topics);
    }

    // GET /topics/top10 - TOP 10 TOPICS
    @GetMapping("/top10")
    public ResponseEntity<List<TopicListResponse>> getTop10Topics() {
        List<TopicListResponse> topics = topicService.findTop10();
        return ResponseEntity.ok(topics);
    }

    // GET /topics/paginated - PAGINATED LISTING
    @GetMapping("/paginated")
    public ResponseEntity<Page<TopicListResponse>> getAllTopicsPaginated(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable) {

        Page<TopicListResponse> topics = topicService.findAllPaginated(pageable);
        return ResponseEntity.ok(topics);
    }

    // GET /topics/{id} - SHOW SPECIFIC TOPIC
    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> getTopicById(@PathVariable Long id) {
        TopicResponse topic = topicService.findById(id);
        return ResponseEntity.ok(topic);
    }

    // POST /topics - CREATE NEW TOPIC
    @PostMapping
    public ResponseEntity<TopicResponse> createTopic(
            @RequestBody @Valid TopicRequest request,
            UriComponentsBuilder uriBuilder) {

        TopicResponse topic = topicService.create(request);

        URI uri = uriBuilder.path("/topics/{id}")
                .buildAndExpand(topic.id())
                .toUri();

        return ResponseEntity.created(uri).body(topic);
    }

    // PUT /topics/{id} - UPDATE TOPIC
    @PutMapping("/{id}")
    public ResponseEntity<TopicResponse> updateTopic(
            @PathVariable Long id,
            @RequestBody @Valid TopicUpdateRequest request) {

        TopicResponse topic = topicService.update(id, request);
        return ResponseEntity.ok(topic);
    }

    // DELETE /topics/{id} - DELETE TOPIC
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /topics/status/{status} - SEARCH BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TopicListResponse>> getTopicsByStatus(@PathVariable String status) {
        Topic.TopicStatus topicStatus = Topic.TopicStatus.valueOf(status.toUpperCase());
        List<TopicListResponse> topics = topicService.findByStatus(topicStatus);
        return ResponseEntity.ok(topics);
    }

    // GET /topics/course/{courseId} - SEARCH BY COURSE
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<TopicListResponse>> getTopicsByCourse(@PathVariable Long courseId) {
        List<TopicListResponse> topics = topicService.findByCourse(courseId);
        return ResponseEntity.ok(topics);
    }

    // GET /topics/search - SEARCH BY TITLE
    @GetMapping("/search")
    public ResponseEntity<List<TopicListResponse>> searchTopics(@RequestParam String title) {
        List<TopicListResponse> topics = topicService.searchByTitle(title);
        return ResponseEntity.ok(topics);
    }

    // GET /topics/my - MY TOPICS
    @GetMapping("/my")
    public ResponseEntity<List<TopicListResponse>> getMyTopics() {
        List<TopicListResponse> topics = topicService.findMyTopics();
        return ResponseEntity.ok(topics);
    }

    // PATCH /topics/{id}/close - CLOSE TOPIC
    @PatchMapping("/{id}/close")
    public ResponseEntity<TopicResponse> closeTopic(@PathVariable Long id) {
        TopicResponse topic = topicService.closeTopic(id);
        return ResponseEntity.ok(topic);
    }

    // PATCH /topics/{id}/solve - MARK AS SOLVED
    @PatchMapping("/{id}/solve")
    public ResponseEntity<TopicResponse> markAsSolved(@PathVariable Long id) {
        TopicResponse topic = topicService.markAsSolved(id);
        return ResponseEntity.ok(topic);
    }

    // GET /topics/{id}/details - TOPIC WITH COMPLETE DETAILS INCLUDING RESPONSES
    @GetMapping("/{id}/details")
    public ResponseEntity<TopicDetailResponse> getTopicDetails(@PathVariable Long id) {
        TopicDetailResponse topic = topicService.findByIdWithDetails(id);
        return ResponseEntity.ok(topic);
    }

    // GET /topics/check/{id} - CHECK IF TOPIC EXISTS
    @GetMapping("/check/{id}")
    public ResponseEntity<Map<String, Object>> checkTopicExists(@PathVariable Long id) {
        try {
            topicService.findById(id); // Se não der erro, existe
            Map<String, Object> response = new HashMap<>();
            response.put("exists", true);
            response.put("topicId", id);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("exists", false);
            response.put("topicId", id);
            return ResponseEntity.ok(response);
        }
    }


}