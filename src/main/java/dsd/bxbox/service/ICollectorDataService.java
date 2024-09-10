package dsd.bxbox.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICollectorDataService {
    void uploadFile(List<MultipartFile> files);
}
