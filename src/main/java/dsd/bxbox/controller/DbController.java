package dsd.bxbox.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@Slf4j
//@CrossOrigin(origins = "http://localhost:3000")
//@AllArgsConstructor
//@RestController
//@RequestMapping("api")
//public class DbController {
//    private final JdbcTemplate jdbc;
//
//    @GetMapping("/sql")
//    public ResponseEntity<?> getSqlTable(@RequestParam String sqlQuery, HttpServletResponse response) {
//        try {
//            List<Map<String, Object>> data = jdbc.queryForList(sqlQuery);
//            if (!response.isCommitted()) return new ResponseEntity<>(data, HttpStatus.OK);
//        } catch (DataAccessException e) {
//            log.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Query is failed. Please try again!");
//        }
//        return null;
//    }
//}

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("api")
public class DbController {
    private final JdbcTemplate jdbc;

    @PostMapping("/sql")
    public ResponseEntity<?> executeSqlQuery(@RequestBody String sqlQuery) {
        try {
            sqlQuery = sqlQuery.trim().toLowerCase();
            if (sqlQuery.startsWith("select")) {
                List<Map<String, Object>> data = jdbc.queryForList(sqlQuery);
                return new ResponseEntity<>(data, HttpStatus.OK);
            } else if (sqlQuery.startsWith("drop")) {
                jdbc.execute(sqlQuery);
                return new ResponseEntity<>("Table dropped successfully", HttpStatus.OK);
            } else {
                jdbc.update(sqlQuery);
                return new ResponseEntity<>("Query executed successfully", HttpStatus.OK);
            }
        } catch (DataAccessException e) {
            log.error("SQL execution error: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Query failed: " + e.getMessage());
        }
    }
}


