package dsd.bxbox.collectors;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileUploader {
    void processFile(List<MultipartFile> file) throws IOException;
}
