package com.example.hireme.Service;

import com.example.hireme.Model.Entity.Media;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Repository.MediaRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final MediaRepository mediaRepository;
    private final LanguageConfig languageConfig;
    @Value("${external.resources.path}")
    private String path;

    @Value("${external.resources.dir}")
    private String folder;
    public void uploadFile(MultipartFile file, Media media, Locale locale) throws IOException {
        if (file.isEmpty() && media.getPath()==null){
            throw new IOException(languageConfig.messageSource().getMessage("cv_required",new Object[] {}, locale));
        }
        if (!file.isEmpty()){
            String filename = file.getOriginalFilename();
            String[] split_name = filename.split("\\.");
            String extension = split_name[split_name.length-1];
            byte[] bytes = file.getBytes();
            String path = this.path+File.separator+media.getType()+File.separator+media.getType()+media.getEntityId()+'.'+extension;
            boolean fileExist = checkFileExistance(path);
            if (fileExist){
                deleteFile(path);
            }
            else {
                media.setPath(File.separator+this.folder+File.separator+media.getType()+File.separator+media.getType()+media.getEntityId()+'.'+extension);
                mediaRepository.save(media);
            }
            Files.write(Paths.get(path), bytes);
        }
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
