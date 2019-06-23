package com.test.demo.controller;

import com.test.demo.model.*;
import com.test.demo.service.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostTest {

    private Company company;
    private Address address;
    private User user;
    private User user2;
    private Post post;
    private Post post2;
    private Post post3;
    private Comment comment;
    private Comment comment2;
    private Comment comment3;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUpAll(){
        company = companyRepository.save(new Company("Romaguera-Crona",
                "Multi-layered client-server neural-net",
                "harness real-time e-markets"));
        address = addressRepository.save(new Address("Kulas Light",
                "Apt. 556",
                "Gwenborough",
                "92998-3874",
                new Point(-37.3159,81.1496)));
        user = userRepository.save(new User("Leanne Graham",
                "Bret",
                "Sincere@april.biz",
                address.getId(),
                "1-770-736-8031 x56442",
                "hildegard.org",
                company.getId()));
        user2 = userRepository.save(new User("Bret Kiano",
                "Leanne",
                "second@april.biz",
                address.getId(),
                "1-770-736-8031 x56442",
                "hildegard.org",
                company.getId()));
        post = postRepository.save(new Post("aut",
                "quia et suscipit\nsuscipit recusandae consequuntur expedita et cu",
                user.getId()));
        post2 = postRepository.save(new Post("aut",
                "quiarecusandae consequuntur expedita et cu",
                user.getId()));
        post3 = postRepository.save(new Post("expedita aut",
                "quiarecusandae consequuntur expedita et cu",
                user.getId()));
        comment = commentRepository.save(new Comment("Comm1","comm1@gmail.com","sunt aut facere repellat",post.getId(),user.getId()));
        comment2 = commentRepository.save(new Comment("Comm2","comm2@gmail.com","nreprehenderit molestiae ut ut",post.getId(),user2.getId()));
        comment3 = commentRepository.save(new Comment("Comm3","comm3@gmail.com","nreprehenderit molestiae ut ut",post3.getId(),user2.getId()));

    }

    @After
    public void clearAll(){
        companyRepository.deleteById(company.getId());
        addressRepository.deleteById(address.getId());

        userRepository.deleteById(user.getId());
        userRepository.deleteById(user2.getId());

        postRepository.deleteById(post.getId());
        postRepository.deleteById(post2.getId());
        postRepository.deleteById(post3.getId());

        commentRepository.deleteById(comment.getId());
        commentRepository.deleteById(comment2.getId());
        commentRepository.deleteById(comment3.getId());
    }

    @Test
    public void getAllPostsByTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/get-all-posts-by-title").param("title","aut")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(post2.getId()));
    }

    @Test
    public void searchPostByTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
        .get("/get-posts-by-search").param("search-word","aut")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(post2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(post3.getId()));

    }

    @Test
    public void deletePostsWithComment() throws Exception {

        Post postDelete = postRepository.save(new Post("aut",
                "quia et suscipit\nsuscipit recusandae consequuntur expedita et cu",
                user.getId()));
        commentRepository.save(new Comment("Comm1","comm1@gmail.com","sunt aut facere repellat",postDelete.getId(),user2.getId()));
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/post/" + postDelete.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deletePostWithCommentsByUser() throws Exception {
        Post postDelete = postRepository.save(new Post("aut",
                "quia et suscipit\nsuscipit recusandae consequuntur expedita et cu",
                user.getId()));
        commentRepository.save(new Comment("Comm1","comm1@gmail.com","sunt aut facere repellat",postDelete.getId(),user2.getId()));
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/post/" + user.getId() + "/" + postDelete.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
