package org.example.services.interfaces;

import org.example.dto.UserDTO;
import org.example.models.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    List<User> readUsersFromJsonFile() throws IOException;
    List<User> getUsers();
    Optional<User> getUser(String id);
    boolean createUser(UserDTO user);
    boolean updateUser(UserDTO user);

    boolean deleteUserById(String id);
    boolean createOrder(UUID user_id);
    Optional<User> getUserByEmail(String email);
}
