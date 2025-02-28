package org.example.service.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.UserReq;
import org.example.entity.user.User;
import org.example.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.example.service.interfaces.IUserService;

import java.io.*;
import java.util.*;

@Service
public class UserService implements IUserService {
    private List<User> users;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger LOGGER = LogManager.getLogger();
    @Value("${user.data.file}")
    private String userDataFile;
    @Autowired
    private final UserRepository userRepository;

    public UserService(ApplicationEventPublisher eventPublisher, UserRepository userRepository) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }
    @PostConstruct
    public void init() throws IOException {
        users=readUsersFromJsonFile();
    }
    @Override
    public List<User> readUsersFromJsonFile()  {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(userDataFile);
        if (file.exists()&& file.length() > 0) {
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
        }else{
            LOGGER.info("User data file not found. Creating new file...");
            if (userRepository.count() > 0) {
                // If users are in the database, save them to a new file
                users = userRepository.findAll();
                writeUsersToJsonFile(users); // Write a new file
                LOGGER.info("New user data file created with existing users from the database.");
            } else {
                LOGGER.info("No users found in the database.");
            }
        }
        return List.of();
    }

    private void writeUsersToJsonFile(List<User> users) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(userDataFile);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (file.createNewFile()) {
                    LOGGER.info("A new file for users is created by the path: " + file.getAbsolutePath());
                } else {
                    LOGGER.warn("Couldn't create a new file. The file may already exist.");
                }
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
            LOGGER.info("Users successfully written to the file.");
        } catch (IOException e) {
            throw new RuntimeException("Error writing users to file: " + e.getMessage(), e);
        }
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
    public boolean createUser(UserReq userReq) {
        LOGGER.info("Current users: " + userRepository.count());
        if(userReq != null && userReq.email() != null && userReq.username() != null) {
            if(userRepository.findByEmail(userReq.email()).isPresent()) {
               return false;
            }
            User user=new User(userReq.username(), userReq.email());
            addUserAndSaveToFile(user);
            //eventPublisher.publishEvent(new UserCreateEvent(this,user));
            return true;
        }
       return  false;
    }

    public boolean updateUser(@NotNull UserReq userReq) {
        if (!userReq.username().isEmpty() && !userReq.email().isEmpty()) {
            Optional<User> userForUpdate = userRepository.findByEmail(userReq.email());
            if (userForUpdate.isPresent()) {
                User user = userForUpdate.get();
                user.setEmail(userReq.email());
                user.setUsername(userReq.username());

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



