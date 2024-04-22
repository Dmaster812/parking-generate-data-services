package validation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import validation.dto.CameraRecordDto;
import validation.dto.ParkingEventDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class ValidationService  {

    @Value("${validation.app.binding:valid-out-0}")
    String binding;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    StreamBridge streamBridge;

    @Bean
    public Consumer<String> validateParkingData() {

        objectMapper.registerModule(new JavaTimeModule());
        log.info("ValidationService binding: {}", binding);

        return (data) -> {

            try {

                CameraRecordDto dto = objectMapper.readValue(data, CameraRecordDto.class);

                log.info("Received record: {}", dto);

                if (isValidCameraRecord(dto)) {

                        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(5);

                        ParkingEventDto parkingEventDto = new ParkingEventDto(dto.getParkingId(),
                                    dto.getCarRegNumber(),
                                    localDateTime.toEpochSecond(ZoneOffset.UTC));

                    streamBridge.send(binding, parkingEventDto);
                }
                else
                    log.error("Invalid record: {}", dto);

            } catch (Exception e) {

                log.error("Error: {}", e.getMessage());

            }
        };
    }

    public static boolean isValidDateTime (LocalDateTime localDateTime){

        return localDateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isValidCameraRecord (CameraRecordDto dto){

        if(dto.getCarRegNumber() == null)
            return true;

        return dto.getCarRegNumber().matches("^(IL \\d{2,3}-\\d{2,3}-\\d{2,3}|null)?")
                && isValidDateTime(dto.getLocalDateTime());
    }
}

