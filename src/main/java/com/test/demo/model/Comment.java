package com.test.demo.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String body;
    @Column
    private Long postId;

    private Long userId;

    public Comment() {
    }

    public Comment(String name, String email, String body, Long postId, Long user_id) {
        this.name = name;
        this.email = email;
        this.body = body;
        this.postId = postId;
        this.userId = user_id;
    }

    public Comment(String name, String email, String body) {
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(name, comment.name) &&
                Objects.equals(email, comment.email) &&
                Objects.equals(body, comment.body) &&
                Objects.equals(postId, comment.postId) &&
                Objects.equals(userId, comment.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, body, postId, userId);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                ", post=" + postId +
                ", user=" + userId +
                '}';
    }
}
