package dsd.bxbox.controller;

import dsd.bxbox.db.ITableVisible;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("api")
public class TableVisibleController {

    private final ITableVisible tableVisible;

    @GetMapping("/tables")
    public ResponseEntity<?> getTableNames() {
        try {
            List<String> tableNames = tableVisible.getTableNames();
            return ResponseEntity.ok(tableNames);
        } catch (DataAccessException e) {
            log.error("Table visible error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Tables visible failed: " + e.getMessage());
        }
    }
}