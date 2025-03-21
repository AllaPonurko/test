package org.example.entity;

import jakarta.persistence.*;
import org.example.entity.dialer.DialerGenre;
import org.example.entity.user.User;
import org.example.enums.GenreTypeEnum;

@Entity
public class InterestTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private GenreTypeEnum topic;
    @ManyToOne
    private DialerGenre dialerGenre;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GenreTypeEnum getTopic() {
        return topic;
    }

    public void setTopic(GenreTypeEnum topic) {
        this.topic = topic;
    }

    public DialerGenre getDialerGenre() {
        return dialerGenre;
    }

    public void setDialerGenre(DialerGenre dialerGenre) {
        this.dialerGenre = dialerGenre;
    }
}
