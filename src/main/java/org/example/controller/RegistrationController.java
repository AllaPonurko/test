package org.example.controller;

import org.example.dto.UserReq;
import org.example.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    public  RegistrationController(UserService _userservice)
    {
        userService=_userservice;
    }
    @PostMapping ("/create")
    public ResponseEntity<String> registerUser(@RequestBody UserReq userReq) {
        if(userService.createUser(userReq))
        {
            return ResponseEntity.ok("User registered successfully!");
        }
        else
            return ResponseEntity.status(400).body("User with email "+userReq.email() +" is already exist.");
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteUserById(@RequestBody String id)
    {
        if(userService.deleteUserAndUpdateFile(id)){
            return ResponseEntity.ok("User deleted successfully!");
        }
        else
            return ResponseEntity.status(500).body("Error!!! User with id: "+id+" doesn't exist.");
    }
}
