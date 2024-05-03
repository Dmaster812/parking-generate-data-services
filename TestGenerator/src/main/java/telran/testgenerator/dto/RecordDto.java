package telran.testgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {

    Integer ParkingId;
    String  CarRegNumber;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime localDateTime;

    @Override
    public String toString() {
        return "CameraRecordDto{" +
                "ParkingId=" + ParkingId +
                ", CarRegNumber='" + CarRegNumber + '\'' +
                ", LocalDateTime=" + localDateTime +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof CameraRecordDto that)) return false;
//        return Objects.equals(getParkingId(), that.getParkingId())
//                && Objects.equals(getCarRegNumber(), that.getCarRegNumber())
//                && Objects.equals(getTimestamp(), that.getTimestamp());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getParkingId(), getCarRegNumber(), getTimestamp());
//    }
}
