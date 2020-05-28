package org.palenyy.entity;

import java.time.LocalDateTime;

public class Note {
    private static final Long UNKNOWN_ID = -1L;

    private final Long id;
    private final String heading;
    private final String text;
    private final LocalDateTime lastEditDateTime;
    private final LocalDateTime creationDateTime;

    public Note(String heading, String text, LocalDateTime lastEditDateTime, LocalDateTime creationDateTime) {
        this(UNKNOWN_ID, heading, text, lastEditDateTime, creationDateTime);
    }

    public Note(Long id, String heading, String text, LocalDateTime lastEditDateTime, LocalDateTime creationDateTime) {
        this.id = id;
        this.heading = heading;
        this.text = text;
        this.lastEditDateTime = lastEditDateTime;
        this.creationDateTime = creationDateTime;
    }

    public Long getId() {
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getLastEditDateTime() {
        return lastEditDateTime;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }
}
