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
    private static final String TABLE_NAME = "notes";
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
        String sql = "SELECT * FROM " + TABLE_NAME;
        return jdbcTemplate.query(sql, new NoteRowMapper());
    }

    @Override
    public Note getById(Long id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID=" + id.toString();
        return jdbcTemplate.queryForObject(sql, new NoteRowMapper());
    }

    @Override
    public void update(Note note) {
        String sql = "UPDATE " + TABLE_NAME + " SET "
                + ENTITY_HEADING + "='" + note.getHeading() + "', "
                + ENTITY_TEXT + "='" + note.getText() + "', "
                + ENTITY_LAST_EDIT + "='" + note.getLastEditDateTime().toString() + "' "
                + "WHERE " + ENTITY_ID + '=' + note.getId().toString();
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID=" + id.toString();
        return jdbcTemplate.update(sql) == 1;
    }

    @Override
    public boolean insert(Note note) {
        String sql = "INSERT INTO " + TABLE_NAME + '('
                + ENTITY_HEADING + ','
                + ENTITY_TEXT + ','
                + ENTITY_LAST_EDIT + ','
                + ENTITY_CREATION + ')'
                + " VALUES("
                + '\'' + note.getHeading() + "', "
                + '\'' + note.getText() + "', "
                + '\'' + note.getLastEditDateTime().toString() + "', "
                + '\'' + note.getCreationDateTime().toString() + "')";
        return jdbcTemplate.update(sql) == 1;
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
