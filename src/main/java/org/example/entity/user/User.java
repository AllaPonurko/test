package org.example.entity.user;

import jakarta.persistence.*;
import org.example.entity.InterestTopic;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String email;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestTopic> listOfInterested;

    public User() {

    }

    public User(String username, String email) {

        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public String toString() {
        return
                "  {" + "id=" + getId() +
                        ", username='" + getUsername() + '\'' +
                        ", email='" + getEmail() + '\'' +
                        '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public List<InterestTopic> getListOfInterested() {
        return listOfInterested;
    }

    public void setListOfInterested(List<InterestTopic> listOfInterested) {
        this.listOfInterested = listOfInterested;
    }
}
