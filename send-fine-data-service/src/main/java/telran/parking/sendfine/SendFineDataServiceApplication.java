package telran.parking.sendfine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import telran.parking.sendfine.dto.Address;
import telran.parking.sendfine.dto.FineDto;
import telran.parking.sendfine.dto.OwnerStatus;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.bouncycastle.asn1.iana.IANAObjectIdentifiers.mail;

@SpringBootApplication
public class SendFineDataServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(SendFineDataServiceApplication.class, args);

    }

}
