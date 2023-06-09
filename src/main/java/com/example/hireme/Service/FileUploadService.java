package com.example.hireme.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {
    public void uploadFile(MultipartFile file) throws IOException {
        try {
            file.transferTo(new File("C:\\Users\\hp\\OneDrive\\Desktop\\Hire_Me\\src\\main\\resources\\media\\cv\\"+file.getOriginalFilename()));
        }
        catch (IOException e){

        }
    }
}
