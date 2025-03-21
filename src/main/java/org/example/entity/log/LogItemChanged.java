package org.example.entity.log;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "log_item_changed")
public class LogItemChanged
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID entityId;
    private String name;
    private LocalDateTime timeOfChange;
    private String reason;
    private String type;

    public LogItemChanged(UUID entityId, String name, LocalDateTime timeOfChange, String reason, String type) {
        this.entityId = entityId;
        this.name = name;
        this.timeOfChange = timeOfChange;
        this.reason = reason;
        this.type = type;
    }

    public LogItemChanged() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeOfChange() {
        return timeOfChange;
    }

    public void setTimeOfChange(LocalDateTime timeOfDelete) {
        this.timeOfChange = timeOfDelete;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
