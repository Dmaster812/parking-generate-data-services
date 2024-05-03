package telran.validation.dto;

import jdk.jfr.Timestamp;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingEventDto {

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
        if (!(o instanceof ParkingEventDto that)) return false;
        return Objects.equals(getParkingId(), that.getParkingId()) && Objects.equals(getCarRegNumber(), that.getCarRegNumber()) && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParkingId(), getCarRegNumber(), getTimestamp());
    }
}
