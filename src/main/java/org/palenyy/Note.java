package org.palenyy;

import java.time.LocalDateTime;

public class Note {
    private String heading;
    private String text;
    private LocalDateTime lastEditDateTime;
    private final String creationDateTime;

    static Note builder(String heading, String text) {
        if (heading.isEmpty() || text.isEmpty()) {
            return null;
        } else {
            LocalDateTime dateTime = LocalDateTime.now();
            return new Note(heading, text, dateTime, dateTime);
        }
    }

    private Note(String heading, String text, LocalDateTime creationDateTime, LocalDateTime lastEditDateTime) {
        this.heading = heading;
        this.text = text;
        this.creationDateTime = creationDateTime.toString();
        this.lastEditDateTime = lastEditDateTime;
    }

    public String getHeading() {
        return heading;
    }

    public String getText() {
        return text;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public String getLastEditDateTime() {
        return lastEditDateTime.toString();
    }

    public void setHeading(String heading) {
        this.heading = heading;
        lastEditDateTime = LocalDateTime.now();
    }

    public void setText(String text) {
        this.text = text;
        lastEditDateTime = LocalDateTime.now();
    }
}
