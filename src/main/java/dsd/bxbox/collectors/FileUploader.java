package dsd.bxbox.collectors;

import dsd.bxbox.db.IDbCreator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Component
public class FileUploader implements IFileUploader {

    private final IDbCreator dbCreator;

    @Override
    public void processFile(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            processOneFile(file);
        }
    }

    private void processOneFile(MultipartFile file){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new BOMInputStream(file.getInputStream()), StandardCharsets.UTF_8))) {
            String tableName = getFileName(file);
            if (dbCreator.isTableExists(tableName)) {
                throw new IllegalStateException("Table already exists in db!");
            }
            String[] headers = reader.readLine().split(",");
            String[] firstRows = reader.readLine().split(",");
            dbCreator.createTable(tableName, headers, firstRows);
            dbCreator.insertData(tableName, headers, reader, firstRows);
            log.info("Success: Uploaded file processed!");
        } catch (IOException e){
            log.error("Error: Uploaded file not processed - " + e);
        }
    }

    private String getFileName(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String name = Objects.requireNonNull(originalName).substring(0, originalName.lastIndexOf("."));
            return originalName.contains(".") ? name : originalName;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
