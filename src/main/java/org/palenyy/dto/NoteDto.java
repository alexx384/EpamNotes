package org.palenyy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class NoteDto {
    private static final Long UNKNOWN_ID = -1L;

    private final Long id;
    private String heading;
    private String text;
    private final LocalDateTime lastEditDateTime;
    private final LocalDateTime creationDateTime;

    public NoteDto(String heading, String text) {
        this(UNKNOWN_ID, heading, text);
    }

    public NoteDto(Long id, String heading, String text) {
        this(id, heading, text, null, null);
    }

    public NoteDto(@JsonProperty("id") Long id,
                   @JsonProperty("heading") String heading,
                   @JsonProperty("text") String text,
                   @JsonProperty("creationDateTime") LocalDateTime creationDateTime,
                   @JsonProperty("lastEditDateTime") LocalDateTime lastEditDateTime) {
        this.id = id;
        this.heading = heading;
        this.text = text;
        this.creationDateTime = creationDateTime;
        this.lastEditDateTime = lastEditDateTime;
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

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public LocalDateTime getLastEditDateTime() {
        return lastEditDateTime;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setText(String text) {
        this.text = text;
    }
}
