package telran.parkingdataservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import telran.parkingdataservice.dto.ParkingDataDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Service
public class ParkingDataService {

        @Value("${max-parking-lots:10}")
        int maxParkingLots;
        @Value("${records-delay:1000}")
        long recordsDelay;


        Map<Integer, ParkingDataDto> parkingDataMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Random random = new Random();

        @Bean
        public Supplier<String> sendParkingData() throws InterruptedException {

            return () -> {

                ParkingDataDto parkingDataDto = getRandomParkingData();

                try {
                    sleep(recordsDelay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if ( parkingDataMap.putIfAbsent(parkingDataDto.getParkingId(), parkingDataDto) != null) {
                    parkingDataDto.setCarRegNumber(null);
                    parkingDataMap.remove(parkingDataDto.getParkingId());
                }

                try {
                    return mapper.writeValueAsString(parkingDataDto);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            };
        }

        private ParkingDataDto getRandomParkingData() {

            long carRegNumber = random.longs(1000000, 90000000)
                    .limit(1)
                    .findAny()
                    .getAsLong();

            return new ParkingDataDto(random.nextInt(1, maxParkingLots),
                    Long.toString(carRegNumber),
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));



        }

}
