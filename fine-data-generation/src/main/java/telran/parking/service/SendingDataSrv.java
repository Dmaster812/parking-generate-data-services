package telran.parking.service;

import jakarta.validation.constraints.Past;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.parking.dto.Address;
import telran.parking.dto.FineDto;
import telran.parking.dto.OwnerStatus;

import static java.lang.Thread.sleep;

@Component
    public class SendingDataSrv {


        Address addressTest = new Address("Haifa", "Teodor Herzel", 123, 12);
        FineDto fineDtoTest = new FineDto(123, OwnerStatus.FREE_PARKING, addressTest, "0534111222","danila.granin@gmail.com",
     124);

        ObjectMapper objectMapper = new ObjectMapper();

        @Bean
        @Qualifier("sendFineData")
        public Supplier<String> generateFineData() {
            return () -> {

                try {
                    sleep(50000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try {
                    System.out.println("Sending FineDataService: " );
                    return objectMapper.writeValueAsString(fineDtoTest);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            };
        }

    }

