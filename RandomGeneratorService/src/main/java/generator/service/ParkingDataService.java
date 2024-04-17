package generator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import validation.dto.CameraRecordDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;

@Service
public class ParkingDataService {

        @Value("${max-parking-lots:10}")
        int maxParkingLots;
        @Value("${records-delay:1000}")
        long recordsDelay;


        Map<Integer, CameraRecordDto> parkingDataMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();

        @Bean
        public Supplier<String> sendParkingData() throws InterruptedException {

            System.out.println("ParkingDataService Max Parking Lots: " + maxParkingLots);
            System.out.println("ParkingDataService Records Delay: " + recordsDelay);

            return () -> {

                objectMapper.registerModule(new JavaTimeModule());
                CameraRecordDto cameraDataDto = getRandomParkingData();

                try {
                    sleep(recordsDelay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if ( parkingDataMap.putIfAbsent(cameraDataDto.getParkingId(), cameraDataDto) != null) {

                    LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(random.nextInt(5, 300));

                    cameraDataDto.setCarRegNumber(null);
                   cameraDataDto.setLocalDateTime(localDateTime.toString());
                   parkingDataMap.remove(cameraDataDto.getParkingId());
                }

                try {
                    return objectMapper.writeValueAsString(cameraDataDto);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            };
        }

        private CameraRecordDto getRandomParkingData() {

            long carRegNumber = random.longs(1000000, 90000000)
                    .limit(1)
                    .findAny()
                    .getAsLong();

            return new CameraRecordDto(random.nextInt(1, maxParkingLots),
                    getIsraelPlateNumber(carRegNumber),
                    LocalDateTime.now().toString());


        }

    private String getIsraelPlateNumber(long carRegNumberLong) {

        StringBuilder carRegNumber = new StringBuilder();

        String carRegNumberString = Long.toString(carRegNumberLong);

        int offset = carRegNumberString.length() / 4 + 1;

        return carRegNumber.append("IL ").append(carRegNumberString)
                .insert(offset + 3, '-').insert(9, '-').toString();
    }

}
