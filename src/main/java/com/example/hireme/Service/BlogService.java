package com.example.hireme.Service;

import com.example.hireme.Repository.BlogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

}
