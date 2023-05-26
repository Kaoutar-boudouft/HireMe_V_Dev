package com.example.hireme.Service;

import com.example.hireme.Repository.BlogTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlogTagService {
    private final BlogTagRepository blogTagRepository;

}
