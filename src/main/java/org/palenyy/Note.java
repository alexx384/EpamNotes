package org.palenyy;

import java.time.LocalDateTime;

public class Note {
    private final String heading;
    private final String text;
    private final String creationDateTime;
    private final String lastEditDateTime;

    static Note builder(String heading, String text) {
        if (heading.isEmpty() || text.isEmpty()) {
            return null;
        } else {
            return new Note(heading, text, LocalDateTime.now(), LocalDateTime.now());
        }
    }

    private Note(String heading, String text, LocalDateTime creationDateTime, LocalDateTime lastEditDateTime) {
        this.heading = heading;
        this.text = text;
        this.creationDateTime = creationDateTime.toString();
        this.lastEditDateTime = lastEditDateTime.toString();
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
        return lastEditDateTime;
    }
}
