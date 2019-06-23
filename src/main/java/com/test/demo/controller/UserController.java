package com.test.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.exception.ExceptionResponse;
import com.test.demo.model.Comment;
import com.test.demo.model.User;
import com.test.demo.model.contract.UserContract;
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

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(
            value = "/get-users-by-post/{post-id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "Get users who write comment under post",
            notes = "Returns 200 if OK or error message",
            response = UserContract.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "Post not found!"),
            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?>getUsersByPost(@ApiParam("Post id") @PathVariable(name = "post-id") Long post_id){
        if(!postRepository.findById(post_id).isPresent()){
            return ExceptionResponse.badRequestError("Post not found!");
        }

        List<Comment> commentsByPost = commentRepository.findByPostId(post_id);

        if(commentsByPost.size() <= 0){
            return ResponseEntity.ok().body("Post don`t have a comment.");
        }

        List<User> result = (List<User>) userRepository.findAllById(commentsByPost.stream().map(Comment::getUserId).collect(Collectors.toList()));
        List<UserContract> contracts = result.stream().map(ModelMapper::mapToContract).collect(Collectors.toList());
        try {
            return ResponseEntity.ok().body(objectMapper.writeValueAsString(contracts));
        } catch (Exception e) {
            return ExceptionResponse.internalError(e.getMessage());
        }

    }
}
