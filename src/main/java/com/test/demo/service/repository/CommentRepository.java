package com.test.demo.service.repository;

import com.test.demo.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {

    List<Comment> findByPostId(Long post_id);
    List<Comment> findByUserId(Long user_id);
    List<Comment> findByBodyContaining(String word);
}
