package com.test.demo.utils;

import com.test.demo.model.Comment;
import com.test.demo.model.Post;
import com.test.demo.model.User;
import com.test.demo.model.contract.CommentContract;
import com.test.demo.model.contract.PostContract;
import com.test.demo.model.contract.UserContract;

public class ModelMapper {

    public static Comment mapToModel(User user, Post post, CommentContract commentContract){
        return new Comment(commentContract.getName(), commentContract.getEmail(), commentContract.getBody(),post.getId(),user.getId());
    }

    public static Comment updateModel(Comment comment, CommentContract commentContract){
        if(commentContract.getName() != null){
            comment.setName(commentContract.getName());
        }
        if(commentContract.getEmail() != null){
            comment.setEmail(commentContract.getEmail());
        }
        if(commentContract.getBody() != null){
            comment.setBody(commentContract.getBody());
        }
        return comment;
    }

    public static UserContract mapToContract(User model){
        UserContract result = new UserContract();
        result.setId(model.getId());

        if(model.getEmail() != null) {
            result.setEmail(model.getEmail());
        }
        if(model.getName() != null){
            result.setName(model.getName());
        }
        if(model.getPhone() != null){
            result.setPhone(model.getPhone());
        }
        if(model.getUsername() != null){
            result.setUsername(model.getUsername());
        }
        if(model.getWebsite() != null){
            result.setWebsite(model.getWebsite());
        }
        if(model.getAddress_id() !=null){
            result.setAddress_id(model.getAddress_id());
        }
        if(model.getCompany_id() != null){
            result.setCompany_id(model.getCompany_id());
        }
        return result;
    }

    public static CommentContract mapToContract(Comment model){
        CommentContract result = new CommentContract();
        result.setId(model.getId());

        if(model.getBody() != null){
            result.setBody(model.getBody());
        }
        if(model.getEmail() != null){
            result.setEmail(model.getEmail());
        }
        if(model.getName() != null){
            result.setName(model.getName());
        }
        if(model.getPostId() != null){
            result.setPost_id(model.getPostId());
        }
        if(model.getUserId() != null){
            result.setUser_id(model.getUserId());
        }
        return result;
    }

    public static PostContract mapToContract(Post model){
        PostContract result = new PostContract();
        result.setId(model.getId());

        if(model.getUser_id() != null){
            result.setUser_id(model.getUser_id());
        }
        if(model.getBody() != null){
            result.setBody(model.getBody());
        }
        if(model.getTitle() != null){
            result.setTitle(model.getTitle());
        }
        return result;
    }
}
