package com.example.hireme.Service;

import com.example.hireme.Repository.NewsLetterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsLetterService {
    private final NewsLetterRepository newsLetterRepository;

}
