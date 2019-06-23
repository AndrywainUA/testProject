package com.test.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentTest {

    private Company company;
    private Address address;
    private User user;
    private Post post;
    private Comment comment;
    private Comment comment2;

    @Autowired
    private ObjectMapper objectMapper;

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
        post = postRepository.save(new Post("sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
                user.getId()));
    }

    @After
    public void clearAll(){
        postRepository.delete(post);
        userRepository.delete(user);
        companyRepository.delete(company);
        addressRepository.delete(address);
    }

    @Test
    public void add() throws Exception {
        comment = new Comment("id labore ex et quam laborum",
                "Eliseo@gardner.biz",
                "laudantium enim quasi est quidem magnam voluptate ipsam eos");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/comment/" + user.getId() + "/" + post.getId())
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/comment/ss" + "/" + post.getId())
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        commentRepository.delete(comment);
    }

    @Test
    public void update() throws Exception {
        comment = new Comment("createComm",
                "create@gmail.com",
                "create Test",
                post.getId(),user.getId());
        commentRepository.save(comment);

        comment.setBody("update Test");
        comment.setName("updateComm");
        comment.setEmail("update@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders
                .put("/comment/qse/" + comment.getId())
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders
            .put("/comment/" + user.getId() + "/" + comment.getId())
            .content(objectMapper.writeValueAsString(comment))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("update@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.body").value("update Test"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("updateComm"));

        commentRepository.delete(comment);
    }

    @Test
    public void delete() throws Exception {
        comment = new Comment("deleteComm",
                "delete@gmail.com",
                "delete Test",
                post.getId(),user.getId());
        commentRepository.save(comment);

        mockMvc.perform(MockMvcRequestBuilders
        .delete("/comment/" + user.getId() + "/" + comment.getId()))
        .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
        .delete("/comment/test/test"))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllCommentsByUser() throws Exception {
        comment = new Comment("Comm1",
                "comm1@gmail.com",
                "comm1 Test",
                post.getId(),user.getId());
        comment2 = new Comment("Comm2",
                "comm2@gmail.com",
                "comm2 Test",
                post.getId(),user.getId());
        commentRepository.save(comment);
        commentRepository.save(comment2);

        mockMvc.perform(MockMvcRequestBuilders
        .get("/get-all-comments-by-user/" + user.getId())
        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(comment.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(comment2.getName()));

        commentRepository.delete(comment);
        commentRepository.delete(comment2);
    }

    @Test
    public void getCountSearchWordInComments() throws Exception {
        comment = new Comment("Comm1",
                "comm1@gmail.com",
                "commTest Test cusandae consequu",
                post.getId(),user.getId());
        comment2 = new Comment("Comm2",
                "comm2@gmail.com",
                "commTest Test scipit Test recusandae consequuntu Test",
                post.getId(),user.getId());
        commentRepository.save(comment);
        commentRepository.save(comment2);
        mockMvc.perform(MockMvcRequestBuilders
        .get("/get-count-search-word-in-all-comments").param("search-word","Test")
                .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("4"));

        commentRepository.delete(comment);
        commentRepository.delete(comment2);
    }
}
