package org.palenyy.dao;

import org.palenyy.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NoteDaoImpl implements NoteDao {
    private static final String ENTITY_ID = "ID";
    private static final String ENTITY_HEADING = "HEADING";
    private static final String ENTITY_TEXT = "TEXT";
    private static final String ENTITY_LAST_EDIT = "LAST_EDIT";
    private static final String ENTITY_CREATION = "CREATION";
    private final JdbcTemplate jdbcTemplate;

    NoteDaoImpl(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Note> getAll() {
        String sql = "SELECT UNIQUE_NOTES.ID, HEADING, TEXT, NOTES.CREATION AS LAST_EDIT, UNIQUE_NOTES.CREATION FROM NOTES, UNIQUE_NOTES WHERE NOTES.ID = UNIQUE_NOTES.LAST_NOTE_ID";
        return jdbcTemplate.query(sql, new NoteRowMapper());
    }

    @Override
    public List<Note> getHistoryById(Long id) {
        String sql = String.format("SELECT HEADING, TEXT, CREATION AS LAST_EDIT FROM NOTES WHERE UNIQUE_ID = %s ORDER BY ID DESC", id.toString());
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> new Note(
                rs.getString(ENTITY_HEADING),
                rs.getString(ENTITY_TEXT),
                rs.getTimestamp(ENTITY_LAST_EDIT).toLocalDateTime()
        ));
    }


    @Override
    public Note getById(Long id) {
        String sql = String.format("SELECT HEADING, TEXT, NOTES.CREATION AS LAST_EDIT, UNIQUE_NOTES.CREATION FROM NOTES, UNIQUE_NOTES WHERE UNIQUE_NOTES.ID = %s AND NOTES.ID = UNIQUE_NOTES.LAST_NOTE_ID", id.toString());
        return jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> new Note(
                id,
                rs.getString(ENTITY_HEADING),
                rs.getString(ENTITY_TEXT),
                rs.getTimestamp(ENTITY_LAST_EDIT).toLocalDateTime(),
                rs.getTimestamp(ENTITY_CREATION).toLocalDateTime()
        ));
    }

    @Override
    public void update(Note note) {
        String preparedSql = "SET REFERENTIAL_INTEGRITY FALSE;\n" +
                "SET @USER_ID = %s;\n" +
                "BEGIN TRANSACTION;\n" +
                "INSERT INTO NOTES (UNIQUE_ID, HEADING, TEXT, CREATION) VALUES (@USER_ID, '%s', '%s', CURRENT_TIMESTAMP());\n" +
                "UPDATE UNIQUE_NOTES SET LAST_NOTE_ID = IDENTITY() WHERE ID = @USER_ID;\n" +
                "COMMIT;\n" +
                "SET REFERENTIAL_INTEGRITY TRUE;";
        String sql = String.format(preparedSql, note.getId().toString(), note.getHeading(), note.getText());
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = String.format("DELETE FROM UNIQUE_NOTES WHERE ID = %s;", id.toString());
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public void insert(Note note) {
        String sql = "SET REFERENTIAL_INTEGRITY FALSE;\n" +
                "BEGIN TRANSACTION;\n" +
                "INSERT INTO UNIQUE_NOTES (LAST_NOTE_ID, CREATION) VALUES (1, '%s');\n" +
                "SET @UNIQUE_NOTE_ID = IDENTITY();\n" +
                "INSERT INTO NOTES (UNIQUE_ID, HEADING, TEXT, CREATION) VALUES (@UNIQUE_NOTE_ID, '%s', '%s', '%s');\n" +
                "SET @NOTE_ID = IDENTITY();\n" +
                "UPDATE UNIQUE_NOTES SET LAST_NOTE_ID = @NOTE_ID WHERE ID = @UNIQUE_NOTE_ID;\n" +
                "COMMIT;\n" +
                "SET REFERENTIAL_INTEGRITY TRUE;";
        sql = String.format(sql,
                note.getCreationDateTime().toString(),
                note.getHeading(),
                note.getText(),
                note.getLastEditDateTime().toString());
        jdbcTemplate.update(sql);
    }

    static class NoteRowMapper implements org.springframework.jdbc.core.RowMapper<Note> {
        @Override
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Note(
                    rs.getLong(ENTITY_ID),
                    rs.getString(ENTITY_HEADING),
                    rs.getString(ENTITY_TEXT),
                    rs.getTimestamp(ENTITY_LAST_EDIT).toLocalDateTime(),
                    rs.getTimestamp(ENTITY_CREATION).toLocalDateTime()
            );
        }
    }
}
