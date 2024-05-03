package telran.parking.sendfine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import telran.parking.sendfine.dto.FineDto;
import java.util.function.Consumer;


@Service
@Slf4j
public class SendFineDataService {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.protocol:smtp}")
    private String protocol;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth:false}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:false}")
    private boolean starttls;

    @Value("${spring.mail.properties.mail.smtp.starttls.required:false}")
    private boolean starttlsRequired;

    @Value("${mail.smtp.ssl.enable:false}")
    private boolean sslEnable;

    @Value("${mail.debug:false}")
    private boolean debug;

    @Value("${parking.info.email: noreply@baeldung.com}")
    String email;


    ObjectMapper objectMapper = new ObjectMapper();
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();


    @Bean
    public Consumer<String> getFineData() {

        setMailSenderProperties();

        log.info("Mail sender: {} {} {} {}", mailSender.getHost(), mailSender.getPort(), mailSender.getProtocol(), mailSender.getJavaMailProperties());

        return (data) -> {

            try {
                FineDto dto = objectMapper.readValue(data, FineDto.class);
                sendMessage(dto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
           }
        };
    }

    private void setMailSenderProperties() {
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setProtocol(protocol);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.getJavaMailProperties().put("mail.smtp.auth", auth);
        mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", starttls);
        mailSender.getJavaMailProperties().put("mail.smtp.starttls.required", starttlsRequired);
        mailSender.getJavaMailProperties().put("mail.smtp.ssl.enable", sslEnable);
        mailSender.getJavaMailProperties().put("mail.debug", debug);
    }

    public void sendMessage(FineDto dto) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(dto.email());
        message.setSubject("Fine Notification");
        message.setText("\n\nDear " + dto.ownerId() + ",\n\n" +
                "You have a fine of " + dto.fineAmount() + " euros.\n\n" +
                "Please pay it in 5 days.\n\n" +
                "Best regards,\n" +
                "Parking Administration");
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }
}
