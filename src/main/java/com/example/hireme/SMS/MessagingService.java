package com.example.hireme.SMS;

import com.vonage.client.VonageClient;
import com.vonage.client.messages.MessageResponse;
import com.vonage.client.messages.MessagesClient;
import com.vonage.client.messages.sms.SmsTextRequest;
import com.vonage.client.messages.viber.Category;
import com.vonage.client.messages.viber.ViberTextRequest;
import com.vonage.client.messages.whatsapp.WhatsappTextRequest;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessagingService {

    @Value("${vonage_api_key}")
    private String API_KEY;

    @Value("${vonage_api_secret}")
    private String API_SECRET;

    public boolean sendSms(String from ,String toNumber, String text) {

        VonageClient client = VonageClient.builder().apiKey(API_KEY.trim()).apiSecret(API_SECRET.trim()).build();


        TextMessage message = new TextMessage(from,
                toNumber,
                text
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            return true;
        } else {
           return false;
        }

    }

//    public static void sendWhatsApp(String toNumber, String text) {
//
//        VonageClient client = VonageClientProvider.getInstance();
//
//        MessagesClient whatsAppClient = client.getMessagesClient();
//
//        var message = WhatsappTextRequest.builder()
//                .from(VONAGE_NUMBER).to(toNumber)
//                .text(text)
//                .build();
//
//        MessageResponse response = whatsAppClient.sendMessage(message);
//        log.info("Message sent successfully. ID: " + response.getMessageUuid());
//    }
}
