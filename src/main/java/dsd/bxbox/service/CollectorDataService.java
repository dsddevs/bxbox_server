package dsd.bxbox.service;

import dsd.bxbox.collectors.IFileUploader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class CollectorDataService implements ICollectorDataService {

    private final IFileUploader fileUploader;

    @Override
    public void uploadFile(List<MultipartFile> files) {
        try {
            fileUploader.processFile(files);
        } catch (IOException e){
            log.error("Error: CollectorDataService: " + e);
        }
    }

}
