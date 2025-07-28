package com.forumhub.controller;

import com.forumhub.dto.response.TopicListResponse;
import com.forumhub.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicListResponse>> getAllTopics() {
        List<TopicListResponse> topics = topicService.findAll();
        return ResponseEntity.ok(topics);
    }


}
