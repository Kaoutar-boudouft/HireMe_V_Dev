package com.example.hireme.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    public String appUrl(HttpServletRequest httpServletRequest) {
        return "http://"+httpServletRequest.getServerName()+":"+httpServletRequest
                .getServerPort()+httpServletRequest.getContextPath();
    }

}
