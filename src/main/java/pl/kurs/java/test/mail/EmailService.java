package pl.kurs.java.test.mail;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.entity.Token;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final TemplateService templateService;

    private final ScheduledExecutorService quickService = Executors.newScheduledThreadPool(NO_OF_QUICK_SERVICE_THREADS);
    @Value("${app.host.url}")
    private String baseUrl;

    private static final String VET_CLINIC_ADDRESS = "vetclinic850@gmail.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private static final int NO_OF_QUICK_SERVICE_THREADS = 20;

    public void sendMessage(String to, String content) throws MessagingException {
        String subject = "Welcome To Vet Clinic!";
        LOGGER.debug("inside sendASynchronousMail method");
        MimeMessage message = emailSender.createMimeMessage();
        message.setFrom(new InternetAddress(VET_CLINIC_ADDRESS));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        MimeBodyPart body = new MimeBodyPart();
        body.setContent(content, "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);
        message.setContent(multipart);

        quickService.submit(() -> {
            try {
                emailSender.send(message);
            } catch (Exception e) {
                LOGGER.error("Exception occur while send a mail : ", e);
            }
        });
    }


    public String messageContent(Token token) throws TemplateException, IOException {
        String urlConfirm = baseUrl + "/visit/" + token.getCode() + "/confirm";
        String confirmVisit = "Click on the link to confirm Visit: " + urlConfirm;

        String urlCancel = baseUrl + "/visit/" + token.getCode() + "/cancel";
        String cancelVisit = "Click on the link to cancel Visit: " + urlCancel;

        Map<String, Object> map = new HashMap<>();
        map.put("confirmationUrl", confirmVisit);
        map.put("canceledUrl", cancelVisit);
        return templateService.fillTemplate("welcome_to_vet_clinic", map);
    }
}
