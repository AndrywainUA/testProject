package com.test.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.exception.ExceptionResponse;
import com.test.demo.model.Comment;
import com.test.demo.model.contract.CommentContract;
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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CommentController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @RequestMapping(
            value = "/comment/{user-id}/{post-id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "User create comment under post",
            notes = "Returns 200 if OK or error message",
            response = CommentContract.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "User not found!"),
                            @ApiResponse(code = 400,message = "Post not found!"),
                            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?> add(@ApiParam("User id") @PathVariable(name = "user-id") Long user_id,
                                 @ApiParam("Post id") @PathVariable(name = "post-id") Long post_id,
                                 @RequestBody CommentContract commentContract){

        if(!userRepository.findById(user_id).isPresent()){
            return ExceptionResponse.badRequestError("User not found!");
        }

        if(!postRepository.findById(post_id).isPresent()){
            return ExceptionResponse.badRequestError("Post not found!");
        }
        try {
            return ResponseEntity.ok().body(commentRepository.save(ModelMapper.mapToModel(userRepository.findById(user_id).get(),
                                                                                          postRepository.findById(post_id).get(),
                                                                                          commentContract)));
        }catch (Exception e){
            return ExceptionResponse.internalError(e.getMessage());
        }
    }

    @RequestMapping(
            value = "/comment/{user-id}/{comment-id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "User update comment under post",
            notes = "Returns 200 if OK or error message",
            response = CommentContract.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "User not found!"),
            @ApiResponse(code = 400,message = "This is not your comment!"),
            @ApiResponse(code = 400,message = "Comment not found!"),
            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?> update(@ApiParam("User id") @PathVariable(name = "user-id") Long user_id,
                                    @ApiParam("Comment id") @PathVariable(name = "comment-id") Long comment_id,
                                    @RequestBody CommentContract commentContract){
        if(!userRepository.findById(user_id).isPresent()){
            return ExceptionResponse.badRequestError("User not found!");
        }

        if(!commentRepository.findById(comment_id).isPresent()){
            return ExceptionResponse.badRequestError("Comment not found!");
        }
        Comment comment = commentRepository.findById(comment_id).get();
        if(!comment.getUserId().equals(user_id)){
            return ExceptionResponse.badRequestError("This is not your comment!");
        }

        try {
            return ResponseEntity.ok().body(commentRepository.save(ModelMapper.updateModel(comment,commentContract)));
        }catch (Exception e){
            return ExceptionResponse.internalError(e.getMessage());
        }
    }

    @RequestMapping(
            value = "/comment/{user-id}/{comment-id}",
            method = RequestMethod.DELETE,
            produces = MediaType.TEXT_PLAIN_VALUE)

    @ApiOperation(
            value = "User delete comment under post",
            notes = "Returns 200 if OK or error message",
            response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "User not found!"),
            @ApiResponse(code = 400,message = "Comment not found!"),
            @ApiResponse(code = 400,message = "This is not your comment!"),
            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?> delete(@ApiParam("User id") @PathVariable(name = "user-id") Long user_id, @ApiParam("Comment id") @PathVariable(name = "comment-id") Long comment_id){
        if(!userRepository.findById(user_id).isPresent()){
            return ExceptionResponse.badRequestError("User not found!");
        }

        if(!commentRepository.findById(comment_id).isPresent()){
            return ExceptionResponse.badRequestError("Comment not found!");
        }
        Comment comment = commentRepository.findById(comment_id).get();
        if(!comment.getUserId().equals(user_id)){
            return ExceptionResponse.badRequestError("This is not your comment!");
        }
        try{
            commentRepository.deleteById(comment_id);
            return ResponseEntity.ok().body("Successfully delete comment_" + comment.getId());
        }catch (Exception e){
            return ExceptionResponse.internalError(e.getMessage());
        }
    }

    @RequestMapping(
            value = "/get-all-comments-by-user/{user-id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "Get all comments by user",
            notes = "Returns 200 if OK or error message",
            response = CommentContract.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "User not found!"),
            @ApiResponse(code = 400,message = "User don`t have a comment!"),
            @ApiResponse(code = 500,message = "Server error!")})
    public ResponseEntity<?> getAllCommentsByUser(@ApiParam("User id") @PathVariable(name = "user-id") Long user_id){
        if(!userRepository.findById(user_id).isPresent()){
            return ExceptionResponse.badRequestError("User not found!");
        }
        List<Comment> comments = commentRepository.findByUserId(user_id);
        if(comments.size() <= 0){
            return ResponseEntity.ok().body("User don`t have a comment!");
        }
        try{
            return ResponseEntity.ok().body(objectMapper.writeValueAsString(comments.stream().map(ModelMapper::mapToContract).collect(Collectors.toList())));
        }catch (Exception e){
            return ExceptionResponse.internalError(e.getMessage());
        }
    }

    @RequestMapping(
            value = "/get-count-search-word-in-all-comments",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(
            value = "Count word in all comments body",
            notes = "Returns 200 if OK or error message",
            response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 400,message = "Search word is empty!")})
    public ResponseEntity<?> getCountWordInAllComments(@ApiParam("Word for search") @RequestParam(name = "search-word") String word){
        if(word == null || word.equals("")){
            return ExceptionResponse.badRequestError("Search word is empty!");
        }
        List<Comment> comments = commentRepository.findByBodyContaining(word);
        long count = comments.stream().flatMap((comment)-> Arrays.stream(comment.getBody().split(" |\\n")).filter(str->str.equals(word))).count();
        return ResponseEntity.ok().body(String.valueOf(count));
    }

}
