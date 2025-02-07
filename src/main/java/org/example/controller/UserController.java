package org.example.controller;

import org.example.dto.UserReq;
import org.example.entity.user.User;
import org.example.response.UserResponse;
import org.example.response.UsersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.service.service.UserService;

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: "+email);
        }
    }
    @GetMapping("/getUsers_")
    public ResponseEntity<?> GetUsers()
    {
        List<User> users=userService.getUsers();
        logger.info("Received request for all users");
        UsersResponse response = new UsersResponse( users);
        if(!users.isEmpty()){logger.debug("Loaded users: {}", users);
            return ResponseEntity.ok(response);}
        else {logger.warn("List users is empty");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users don't exist");}
    }
    @GetMapping("/getUsers")
    public List<User> GetUsersList()
    {
        List<User> users=userService.getUsers();
        if(!users.isEmpty()) {
            logger.info("List users is downloaded");
            return users;}
        else {
            logger.info("Users don't exist");
            return null;}
    }
    @PutMapping("/updateUser")
    public ResponseEntity<?> UpdateUser(@RequestBody UserReq userReq){
        if(userService.updateUser(userReq)){
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
