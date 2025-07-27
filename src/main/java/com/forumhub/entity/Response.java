package com.forumhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "responses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Response message is required")
    @Size(min = 10, message = "Response message must have at least 10 characters")
    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private Boolean solution = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //Constructor with main parameters
    public Response(String message, Topic topic, User author){
        this.message = message;
        this.topic = topic;
        this.author = author;
        this.solution = false;
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void markAsSolution() {
        this.solution = true;
        // When marked as solution, mark the topic as solved
        if (this.topic != null) {
            this.topic.markAsSolved();
        }
    }

    public void unmarkAsSolution() {
        this.solution = false;
    }

    public boolean isSolution() {
        return this.solution != null && this.solution;
    }


}
