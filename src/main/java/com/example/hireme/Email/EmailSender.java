package com.example.hireme.Email;

public interface EmailSender {
    void send(String to, String content, String subject, String from);
}
