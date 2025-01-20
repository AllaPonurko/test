package org.example.controller;

import org.example.dto.UserDTO;
import org.example.models.User;
import org.example.responses.UserResponse;
import org.example.responses.UsersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.services.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    @Autowired
    public UserController(UserService _userService){
        userService=_userService;
        logger.info("UserController initialized!");
    }
    @GetMapping("/getUser")
    public ResponseEntity<String> getUserById(@RequestParam String id){
        logger.debug("Received request for user with id: {}", id);
        Optional<User> user = userService.getUser(id);

        if (user.isPresent()) {
            UserResponse response = new UserResponse("User found: ", user.get());
            return ResponseEntity.ok(String.valueOf(response));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: "+id);
        }
    }
    @GetMapping("/getUserByEmail")
    public ResponseEntity<String> getUserByEmail(@RequestParam String email){
        logger.debug("Received request for user with email: {}", email);
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            UserResponse response = new UserResponse("User found.", user.get());
            return ResponseEntity.ok(String.valueOf(response));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: "+email);
        }
    }
    @GetMapping("/getUsers_")
    public ResponseEntity<?> GetUsers()
    {
        List<User> users=userService.getUsers();
        logger.info("Received request for all users");
        logger.debug("Loaded users: {}", users);
        UsersResponse response = new UsersResponse( users);
        if(!users.isEmpty()) return ResponseEntity.ok(response);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users don't exist");
    }
    @GetMapping("/getUsers")
    public List<User> GetUsersList()
    {
        List<User> users=userService.getUsers();

        if(!users.isEmpty()) return users;
        else return null;
    }
    @PutMapping("/updateUser")
    public ResponseEntity<?> UpdateUser(@RequestBody UserDTO userDTO){
        if(userService.updateUser(userDTO)){
            return  ResponseEntity.ok().body("User data successfully updated.");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User data could not be updated.");
    }
    @GetMapping("/test")
    public String testEndpoint() {
        return "API is working";
    }
    @PostMapping("/createOrder")
    public ResponseEntity<?> CreateOrder(@RequestBody UUID user_id)
    {
        if(userService.createOrder(user_id)){
        return  ResponseEntity.ok().body("Order successfully created.");
        }
        return ResponseEntity.status(500).body("Failed to create an order.");
    }
}
