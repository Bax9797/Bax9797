package pl.kurs.java.test.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.entity.Token;


@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String VET_CLINIC_ADDRESS = "vetclinic850@gmail.com";

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String text) {
        String subject = "Welcome To Vet Clinic!";
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(VET_CLINIC_ADDRESS);
            message.setTo(to);
            message.setText(text);
            message.setSubject(subject);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    public String messageContent(Token token) {
        String urlConfirm = "http://localhost:8000/visit/confirm/" + token.getCode();
        String confirmVisit = "Click on the link to confirm Visit " + urlConfirm;

        String urlCancel = "http://localhost:8000/visit/cancel/" + token.getCode();
        String cancelVisit = "Click on the link to cancel Visit " + urlCancel;

        String message = confirmVisit + "  " + cancelVisit;
        return message;
    }
}

