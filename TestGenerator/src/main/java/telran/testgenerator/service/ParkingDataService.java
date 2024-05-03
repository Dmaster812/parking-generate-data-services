package telran.testgenerator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import telran.testgenerator.dto.RecordDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;

@Service
public class ParkingDataService {

    private static final Logger log = LoggerFactory.getLogger(ParkingDataService.class);

//  Max number of parking lots
        @Value("${max-parking-lots:10}")
        int maxParkingLots;

//  Delay between generated records
        @Value("${records-delay:1000}")
        long recordsDelay;
        int parkingId;
        int parkingIdToRemove = 0;


        Map<Integer, RecordDto> parkingDataMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();

        @Bean
        public Supplier<String> sendParkingData() throws InterruptedException {

            log.info("ParkingDataService Max Parking Lots: {}", maxParkingLots);
            log.info("ParkingDataService Records Delay: {}", recordsDelay);

            return () -> {

                objectMapper.registerModule(new JavaTimeModule());
                RecordDto cameraDataDto = getRandomParkingData();

                try {
                    sleep(recordsDelay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                parkingId = cameraDataDto.getParkingId();

                // Parking ID was marked for removal
                if (parkingIdToRemove != 0) {
                    parkingDataMap.remove(parkingIdToRemove);
                    parkingIdToRemove = 0;
                }

                if (parkingDataMap.containsKey(parkingId)) {

                    // Update the record with new time
                    LocalDateTime localDateTime = parkingDataMap.get(parkingId).getLocalDateTime();
                    parkingDataMap.get(parkingId).setLocalDateTime(localDateTime.plusMinutes(random.nextInt(5, 30)));

                    int selector = random.nextInt(1, 100);

                    if (selector > 80) {
                        // 20% chances that the parking lot became empty
                        parkingDataMap.get(parkingId).setCarRegNumber(null);

                    } else if (selector > 40 &&
                            parkingDataMap.get(parkingId).getCarRegNumber() == null) {
                        // 40% chances that a new car arrived
                        parkingIdToRemove = parkingId;
                    }

                }
                    else
                        parkingDataMap.put(cameraDataDto.getParkingId(), cameraDataDto);

                try {

                    System.out.println(parkingDataMap.get(parkingId));
                    return objectMapper.writeValueAsString(parkingDataMap.get(parkingId));

                } catch (Exception e) {

                    log.error("Error: {}", e.getMessage());

                }

                return "";
            };

        }

        private RecordDto getRandomParkingData() {

            long carRegNumber = random.longs(1000000, 99999999)
                    .limit(1)
                    .findAny()
                    .getAsLong();

            return new RecordDto(random.nextInt(1, maxParkingLots),
                    getIsraelPlateNumber(carRegNumber),
                    LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));


        }

    private String getIsraelPlateNumber(long carRegNumberLong) {

        StringBuilder carRegNumber = new StringBuilder();

        String carRegNumberString = Long.toString(carRegNumberLong);

        int offset = carRegNumberString.length() / 4 + 1;

        return carRegNumber.append("IL ").append(carRegNumberString)
                .insert(offset + 3, '-').insert(9, '-').toString();

        }

}
