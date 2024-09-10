package dsd.bxbox.controller;


import dsd.bxbox.service.ICollectorDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000/")
@AllArgsConstructor
@RestController
@RequestMapping("api")
public class CollectorDataController {

    private ICollectorDataService collectorDataService;

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        String successInfo = "File data stored in db!";
        try {
            collectorDataService.uploadFile(files);
            log.info(successInfo);
            return ResponseEntity.ok(successInfo);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
