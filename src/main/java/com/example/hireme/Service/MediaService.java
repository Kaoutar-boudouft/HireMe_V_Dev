package com.example.hireme.Service;

import com.example.hireme.Model.Entity.Media;
import com.example.hireme.Repository.MediaRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;

    public Media getMedia(String entity ,Long entity_id ,String type){
       return mediaRepository.findByEntityAndEntityIdAndType(entity ,entity_id ,type);
    }

    public void deleteMedia(Media media){
        mediaRepository.delete(media);
    }
}
