package com.example.hireme.Service;

import com.example.hireme.Model.Entity.Media;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Repository.MediaRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final MediaRepository mediaRepository;
    private final LanguageConfig languageConfig;
    public void uploadFile(MultipartFile file, Media media) throws IOException {
        if (file.isEmpty()){
            throw new IOException(languageConfig.messageSource().getMessage("cv_required",new Object[] {}, null));
        }
            String filename = file.getOriginalFilename();
            String[] split_name = filename.split("\\.");
            String extension = split_name[split_name.length-1];
            byte[] bytes = file.getBytes();
            String path = "C:\\Users\\hp\\OneDrive\\Desktop\\Hire_Me\\src\\main\\resources\\static\\assets\\media\\"+media.getType()+"\\"+media.getType()+media.getEntityId()+'.'+extension;
            boolean fileExist = checkFileExistance(path);
            if (fileExist){
                deleteFile(path);
            }
            else {
                media.setPath("\\static\\assets\\media\\"+media.getType()+"\\"+media.getType()+media.getEntityId()+'.'+extension);
                mediaRepository.save(media);
            }
            Files.write(Paths.get(path), bytes);
    }

    public boolean checkFileExistance(String path){
        File f = new File(path);
        if(f.exists()) {
           return true;
        }
        return false;
    }

    public void deleteFile(String path){
            File fileToDelete = new File(path);
            fileToDelete.delete();
    }
}
