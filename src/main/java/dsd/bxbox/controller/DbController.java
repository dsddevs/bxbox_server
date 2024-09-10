package dsd.bxbox.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("api")
public class DbController {
    private final JdbcTemplate jdbc;

    @GetMapping("/sql")
    public ResponseEntity<?> getSqlTable(@RequestParam String sqlQuery, HttpServletResponse response) {
        try {
            List<Map<String, Object>> data = jdbc.queryForList(sqlQuery);
            if (!response.isCommitted()) return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Query is failed. Please try again!");
        }
        return null;
    }
}
