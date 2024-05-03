package telran.validation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CameraRecordDto {

    Integer ParkingId;
    String CarRegNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime localDateTime;

    @Override
    public String toString() {

        return "CameraRecordDto{" +
                "ParkingId=" + ParkingId +
                ", CarRegNumber='" + CarRegNumber + '\'' +
                ", LocalDateTime=" + localDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CameraRecordDto that)) return false;
        return Objects.equals(getParkingId(), that.getParkingId()) && Objects.equals(getCarRegNumber(), that.getCarRegNumber()) && Objects.equals(getLocalDateTime(), that.getLocalDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParkingId(), getCarRegNumber(), getLocalDateTime());
    }
}
