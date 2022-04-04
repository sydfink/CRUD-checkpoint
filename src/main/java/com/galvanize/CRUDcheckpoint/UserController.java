package com.galvanize.CRUDcheckpoint;

import com.galvanize.CRUDcheckpoint.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository){
        this.repository = repository;
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers(){
        return repository.findAll();
    }

    @PostMapping("/users")
    public User saveUser(@RequestBody User user){
        return repository.save(user);
        //User newUser = repository.save(user); return new ResponseEntity
    }

    @GetMapping("/users/{id}")
    public Optional<User> getOneUserNoPassword(@PathVariable int id){
        return repository.findById(id);
    }

    @PatchMapping("/users/{id}")
    public User updateOneUser(@PathVariable int id, @RequestBody Map<String, String> users){
        //retrieve old user
        User oldUser = this.repository.findById(id).get();
        //check passed in info to update
        users.forEach((key, value) -> {
            if (key.equals("email")){
                oldUser.setEmail(value);
            } else if ((key.equals("password"))) {
                oldUser.setPassword(value);
            }
        });
        //save changed entry
        return this.repository.save(oldUser);
    }

    @DeleteMapping("/users/{id}")
    public HashMap<String, Integer> deleteOneUser(@PathVariable int id){
        repository.deleteById(id);
        HashMap<String,Integer> deleted = new HashMap<>();
        deleted.put("count", (int) repository.count());
        return deleted;
    }

    @PostMapping("/users/authenticate")
    public Authentication userAuth(@RequestBody User potentialUser) {
        Authentication yayOrNay = new Authentication();
        if (repository.findUserByEmail(potentialUser.getEmail()).getPassword().equals(potentialUser.getPassword())) {
            yayOrNay.setAuthenticated(true);
            yayOrNay.setUser(repository.findUserByEmail(potentialUser.getEmail()));
        } else {
            yayOrNay.setAuthenticated(false);
        }
        return yayOrNay;
    }


}
