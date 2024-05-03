package telran.parking.sendfine.dto;

// ================== CLASS ==================
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//public class FineDto {
//
//    Integer ownerId;
//    OwnerStatus ownerStatus;
//    Address ownerAddress = new Address();
//    String phone;
//    String email;
//    Integer fineAmount;
//}
// ================== CLASS ==================


public record FineDto (
        Integer ownerId,
        OwnerStatus ownerStatus,
        Address ownerAddress,
        String phone,
        String email,
        Integer fineAmount) {}