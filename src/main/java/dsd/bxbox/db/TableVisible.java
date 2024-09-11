package dsd.bxbox.db;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableVisible implements ITableVisible{

    private final JdbcTemplate jdbcTemplate;

    public TableVisible(JdbcTemplate jdbcTemplate)  {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> getTableNames() {
        String sql = "SHOW TABLES";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
