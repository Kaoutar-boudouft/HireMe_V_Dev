package com.example.hireme.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class FileUploadService {
    public void uploadFile(MultipartFile file,Long user_id) throws IOException {
        try {
            String filename = file.getOriginalFilename();
            String[] split_name = filename.split("\\.");
            String extension = split_name[split_name.length-1];
            byte[] bytes = file.getBytes();
            Files.write(Paths.get("C:\\Users\\hp\\OneDrive\\Desktop\\Hire_Me\\src\\main\\resources\\media\\cv\\"+"CV"+user_id+'.'+extension), bytes);
        }
        catch (IOException e){

        }
    }
}
