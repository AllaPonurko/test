package org.example.entity.order;

import jakarta.persistence.*;
import org.example.listener.EntityChangesListener;

import java.util.UUID;

@MappedSuperclass
public class OrderBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
