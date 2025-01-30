package org.example.services.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.UserDTO;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.example.services.interfaces.IUserService;

import java.io.*;
import java.util.*;

@Service
public class UserService implements IUserService {
    private List<User> users;
    private static final Logger LOGGER = LogManager.getLogger();
    @Value("${user.data.file}")
    private String userDataFile;
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @PostConstruct
    public void init() throws IOException {
        users=readUsersFromJsonFile();
    }
    @Override
    public List<User> readUsersFromJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(userDataFile);
        if (file.exists()) {
            try {
                users = objectMapper.readValue(file, new TypeReference<List<User>>() {
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (userRepository.count() == 0) {
                userRepository.saveAll(users);
                LOGGER.info("Users imported into the database.");
            } else {
                LOGGER.info("Users already exist in the database.");
            }
            return users;
        }
        return List.of();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUser(String userIdString) {
        LOGGER.info("Searching for user with id: " + userIdString);
        try {
            UUID id = UUID.fromString(userIdString);
            LOGGER.info("Converted UUID: " + id);
            Optional<User> user = userRepository.findById(id);
            LOGGER.info("User found: " + user.isPresent());
            return user;
        } catch (IllegalArgumentException e) {
            LOGGER.info("Invalid UUID format: " + userIdString);
            return Optional.empty();
        }
    }

    @Override
    public boolean createUser(UserDTO userDTO) {
        LOGGER.info("Current users: " + userRepository.count());
        if(userDTO != null && userDTO.email() != null && userDTO.username() != null) {
            if(userRepository.findByEmail(userDTO.email()).isPresent()) {
                return false;
            }
            User user=new User(userDTO.username(), userDTO.email());
            addUserAndSaveToFile(user);
            return true;
        }
       return  false;
    }

    public boolean updateUser(@NotNull UserDTO userDTO) {
        if (!userDTO.username().isEmpty() && !userDTO.email().isEmpty()) {
            Optional<User> userForUpdate = userRepository.findByEmail(userDTO.email());
            if (userForUpdate.isPresent()) {
                User user = userForUpdate.get();
                user.setEmail(userDTO.email());
                user.setUsername(userDTO.username());

                addUserAndSaveToFile(user);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    private void addUserToFile(User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(userDataFile);
            if (file.exists()) {
                users = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
            }
            if (users == null) {
                users = new ArrayList<>();
            }
            users.add(user);
            objectMapper.writeValue(file, users);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving user data to file: " + e.getMessage());
        }
    }
    public void addUserAndSaveToFile(@NotNull User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        userRepository.save(user);
        addUserToFile(user);
    }
    @Override
    public boolean deleteUserById(String userIdString) {
        try {
            UUID id = UUID.fromString(userIdString);
            Optional<User> userToRemove = getUser(id.toString());
            if (userToRemove.isPresent()) {
                   userRepository.delete(userToRemove.get());
                LOGGER.info("User deleted from database.");

                   return true;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.info("Invalid UUID format: " + userIdString);
        }
        return false;
    }

    @Override
    public boolean createOrder(UUID user_id) {

        return false;
    }

    public boolean deleteUserAndUpdateFile(String userIdString) {
        boolean isDeleted = deleteUserById(userIdString);
        if (isDeleted) {
            updateUserFile();
            return true;
        } else {
            LOGGER.info("Failed to delete user or user not found.");
            return false;
        }
    }
    public void updateUserFile() {
        try {
            List<User> allUsers = getUsers();
            writeUsersToFile(allUsers);
            LOGGER.info("User file updated.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating user file: " + e.getMessage());
        }
    }
    private void writeUsersToFile(List<User> users) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(userDataFile);
            objectMapper.writeValue(file, users);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving user data to file: " + e.getMessage());
        }
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        System.out.println("Searching for user with email: " + email);
        try {
            Optional<User> user = userRepository.findByEmail(email);
            LOGGER.info("User found: " + user.isPresent());
            return user;
        } catch (IllegalArgumentException e) {

            return Optional.empty();
        }
    }
}



