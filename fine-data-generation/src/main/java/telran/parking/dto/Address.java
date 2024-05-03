package telran.parking.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Address implements Serializable {

    String city;
    String street;
    Integer building;
    Integer apps;

    public Address(String city, String street, Integer building, Integer apps) {
        super();
        this.city = city;
        this.street = street;
        this.building = building;
        this.apps = apps;
    }

}