package com.galvanize.CRUDcheckpoint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository repository;

    @Test
    @Transactional
    @Rollback
    void addNewUsersTest() throws Exception{
        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"john@example.com\", \"password\": \"something-secret\"}"))
                //post status is 201 -- isCreated
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email", is("john@example.com")));
                //.andExpect(jsonPath("$.password", is("something-secret"))); is ignored from @JsonIgnore in User class
    }

    @Test
    @Transactional
    @Rollback
    void getAllUsersTest() throws Exception{
    User testUser = new User("john@example.com", "something-secret");
    User record = this.repository.save(testUser);
    User testUser2 = new User("eliza@example.com", "something-else-secret");
    User record2 = this.repository.save(testUser2);

    this.mvc.perform(get("/users"))
            //get status is 200 -- isOk
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email", is("john@example.com")))
            //.andExpect(jsonPath("$[0].password", is("something-secret"))) @JsonIgnore
            .andExpect(jsonPath("$[0].id", is(record.getId())))
            .andExpect(jsonPath("$[1].email", is("eliza@example.com")))
            //.andExpect(jsonPath("$[1].password", is("something-else-secret"))) @JsonIgnore
            .andExpect(jsonPath("$[1].id", is(record2.getId())));

    }

    @Test
    @Transactional
    @Rollback
    void getOneUserNoPasswordTest() throws Exception{
        User testUser = new User("john@example.com", "something-super-secret");
        User record = this.repository.save(testUser);

        this.mvc.perform(get("/users/" + record.getId()))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.password").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.id", is(record.getId())));

    }

    @Test
    @Transactional
    @Rollback
    void patchUpdatesUsersTest() throws Exception{
        User testUser = new User("john@example.com", "something-secret");
        User record = this.repository.save(testUser);

        this.mvc.perform(patch("/users/" + record.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"nik@example.com\", \"password\": \"something-extra-secret\"}"))
                .andExpect(jsonPath("$.email", is("nik@example.com")))
                .andExpect(jsonPath("$.id", is(record.getId())));
    }

    @Test
    @Transactional
    @Rollback
    void DeleteUsersTest() throws Exception{
        User testUser = new User("john@example.com", "something-secret");
        User record = this.repository.save(testUser);
        User testUser2 = new User("eliza@example.com", "something-else-secret");
        User record2 = this.repository.save(testUser2);


        this.mvc.perform(delete("/users/" + record.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"john@example.com\", \"password\": \"something-secret\"}"))
                .andExpect(jsonPath("$.count", is(1)));
    }

    @Test
    @Transactional
    @Rollback
    void authenticateUserTrueTest() throws Exception{
        User testUser = new User("john@example.com", "something-secret");
        User record = this.repository.save(testUser);
        this.mvc.perform(post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"john@example.com\", \"password\": \"something-secret\"}"))
                .andExpect(jsonPath("$.user.email", is("john@example.com")))
                .andExpect(jsonPath("$.user.id").isNumber())
                .andExpect(jsonPath("$.authenticated", is(true)));




    }

    @Test
    @Transactional
    @Rollback
    void authenticateUserFalseTest() throws Exception{
        User testUser = new User("john@example.com", "something-secret");
        User record = this.repository.save(testUser);
        this.mvc.perform(post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"john@example.com\", \"password\": \"secret\"}"))
                .andExpect(jsonPath("$.user.id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.authenticated", is(false)));


    }
}
