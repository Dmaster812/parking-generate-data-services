package telran.parkingdataservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.executable.ValidateOnExecution;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class ParkingDataDto {

    Integer ParkingId;
    String  CarRegNumber;
    @Timestamp
    Long  timestamp;

    @Override
    public String toString() {
        return "ParkingDataDto{" +
                "ParkingId=" + ParkingId +
                ", CarRegNumber='" + CarRegNumber + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingDataDto that)) return false;
        return Objects.equals(getParkingId(), that.getParkingId()) && Objects.equals(getCarRegNumber(), that.getCarRegNumber()) && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParkingId(), getCarRegNumber(), getTimestamp());
    }
}
