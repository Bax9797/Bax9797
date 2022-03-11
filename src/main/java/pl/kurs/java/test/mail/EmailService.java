package pl.kurs.java.test.mail;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.entity.Token;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String VET_CLINIC_ADDRESS = "vetclinic850@gmail.com";
    private final JavaMailSender emailSender;

    @Async
    public void sendMessage(String to, String text) {
        String subject = "Welcome To Vet Clinic!";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(VET_CLINIC_ADDRESS);
        message.setTo(to);
        message.setText(text);
        message.setSubject(subject);
        emailSender.send(message);
    }

    public String messageContent(Token token) {
        String urlConfirm = "http://localhost:8000/visit/" + token.getCode() + "/confirm";
        String confirmVisit = "Click on the link to confirm Visit " + urlConfirm;

        String urlCancel = "http://localhost:8000/visit/" + token.getCode() + "/cancel";
        String cancelVisit = "Click on the link to cancel Visit " + urlCancel;

        String message = confirmVisit + "  " + cancelVisit;
        return message;
    }
}

