package com.galvanize.CRUDcheckpoint;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner commandLineRunnerUsers(UserRepository userRepository) {
        return args -> {
            User user = new User("john@example.com", "something-secret");
            User user2 = new User("eliza@example.com", "something-else-great");
            userRepository.saveAll(List.of(user, user2));
        };
    }

}
