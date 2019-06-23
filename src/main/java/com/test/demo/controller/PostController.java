package com.test.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.exception.ExceptionResponse;
import com.test.demo.model.Comment;
import com.test.demo.model.Post;
import com.test.demo.service.repository.CommentRepository;
import com.test.demo.service.repository.PostRepository;
import com.test.demo.service.repository.UserRepository;
import com.test.demo.utils.ModelMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @RequestMapping(
            value = "/post/{user-id}/{post_id}",
            method = RequestMethod.DELETE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(
            value = "User delete post with all comment",
            notes = "Returns 200 if OK or error message",
            response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "User not found!"),
            @ApiResponse(code = 400,message = "Post not found!"),
            @ApiResponse(code = 400,message = "This is not your post!"),
            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?> deletePostWithCommentByUser(@ApiParam("User id") @PathVariable(name = "user-id") Long user_id,
                                                         @ApiParam("Post id") @PathVariable(name = "post_id") Long post_id){
        if(!userRepository.findById(user_id).isPresent()){
            return ExceptionResponse.badRequestError("User not found!");
        }

        if(!postRepository.findById(post_id).isPresent()){
            return ExceptionResponse.badRequestError("Post not found!");
        }

        Post post = postRepository.findById(post_id).get();
        if(!post.getUser_id().equals(user_id)){
            return ExceptionResponse.badRequestError("This is not your post!");
        }

        return deletePostWithComments(post_id);
    }

    @RequestMapping(
            value = "/post/{post-id}",
            method = RequestMethod.DELETE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(
            value = "Delete post by id with all comment",
            notes = "Returns 200 if OK or error message",
            response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "Post not found!"),
                            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?> deletePostWithComment(@ApiParam("Post id") @PathVariable(name = "post-id") Long post_id){
        if(!postRepository.findById(post_id).isPresent()){
            return ExceptionResponse.badRequestError("Post not found!");
        }
        return deletePostWithComments(post_id);
    }

    @RequestMapping(
            value = "/get-all-posts-by-title",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "Get all posts by title",
            notes = "Returns 200 if OK or error message",
            response = Post.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "Title is empty!"),
                            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?> getAllPostsByTitle(@ApiParam("Title for search") @RequestParam(name = "title") String title){
        if(title == null || title.equals("")){
            ExceptionResponse.badRequestError("Title is empty!");
        }

        List<Post> result = postRepository.findByTitle(title);
        if(result.size() <= 0){
            return ResponseEntity.ok().body("Don`t find any post with title '" + title + "'");
        }
        try{
            return ResponseEntity.ok().body(objectMapper.writeValueAsString(result.stream().map(ModelMapper::mapToContract).collect(Collectors.toList())));
        }catch (Exception e){
            return ExceptionResponse.internalError(e.getMessage());
        }
    }

    @RequestMapping(
            value = "/get-posts-by-search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "Search by word in post title",
            notes = "Returns 200 if OK or error message",
            response = Post.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "Title is empty!"),
            @ApiResponse(code = 200,message = "Don`t find any post with word 'your word'"),
            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?> searchPostsByTitle(@ApiParam("Word for search") @RequestParam(name = "search-word") String word){
        if(word == null || word.equals("")){
            return ExceptionResponse.badRequestError("Search word is empty!");
        }
        List<Post> posts = postRepository.findByTitleContaining(word);
        if(posts.size() <= 0){
           return  ResponseEntity.ok().body("Don`t find any post with word '" + word + "'");
        }
        try {
            return ResponseEntity.ok().body(objectMapper.writeValueAsString(posts.stream().map(ModelMapper::mapToContract)));
        }catch (Exception e){
            return ExceptionResponse.internalError(e.getMessage());
        }
    }

    private ResponseEntity<?> deletePostWithComments(Long post_id) {
        try {
            List<Comment> listComment = commentRepository.findByPostId(post_id);
            if(listComment.size() > 0){
                commentRepository.deleteAll(listComment);
            }
            postRepository.deleteById(post_id);
            return ResponseEntity.ok().body("Successfully delete post_" + post_id + " and all comment(" + listComment.size() + ")!");
        }catch (Exception e){
            return ExceptionResponse.internalError(e.getMessage());
        }
    }
}
