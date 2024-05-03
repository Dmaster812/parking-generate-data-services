
//=======================================================
//package telran.parking.dto;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import telran.parking.dto.OwnerStatus;
//import telran.parking.dto.Address;
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
//=======================================================

package telran.parking.dto;

import java.io.Serializable;

public record FineDto  (
        Integer ownerId,
        OwnerStatus ownerStatus,
        Address ownerAddress,
        String phone,
        String email,
        Integer fineAmount)  {}
