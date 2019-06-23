package com.test.demo.service.repository;

import com.test.demo.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post,Long> {

    List<Post> findByTitle(String title);
    List<Post> findByTitleContaining(String searchWord);
}
