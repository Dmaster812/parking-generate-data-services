package validation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import validation.dto.CameraRecordDto;
import validation.dto.ParkingEventDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@Service
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
        System.out.println("ValidationService binding: " + binding);

        return (data) -> {

            try {

                CameraRecordDto dto = objectMapper.readValue(data, CameraRecordDto.class);

                System.out.println("Received data: " + dto);

                if (isValidCameraRecord(dto)) {

                        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(5);

                        ParkingEventDto parkingEventDto = new ParkingEventDto(dto.getParkingId(),
                                    dto.getCarRegNumber(),
                                    localDateTime.toEpochSecond(ZoneOffset.UTC));

                    streamBridge.send("valid-out-0", parkingEventDto);
                }
                else
                    log.error("Invalid record: {}", dto);

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public static boolean isValidDateTime (String dateTimeString){
        try {
            LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidCameraRecord (CameraRecordDto dto){

        if(dto.getCarRegNumber() == null)
            return true;

        return dto.getCarRegNumber().matches("^(IL \\d{2,3}-\\d{2,3}-\\d{2,3}|null)?")
                && isValidDateTime(dto.getLocalDateTime());
    }
}

