package org.example.controller;

import org.example.dto.UserDTO;
import org.example.services.services.UserService;
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
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        if(userService.createUser(userDTO))
        {
            return ResponseEntity.ok("User registered successfully!");
        }
        else
            return ResponseEntity.status(500).body("The user already exists.");
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
