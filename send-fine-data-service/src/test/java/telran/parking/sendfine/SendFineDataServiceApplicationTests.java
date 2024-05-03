package telran.parking.sendfine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import telran.parking.sendfine.dto.Address;
import telran.parking.sendfine.dto.FineDto;
import telran.parking.sendfine.dto.OwnerStatus;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class SendFineDataServiceApplicationTests {

    final Address ADDRESS = new Address("Haifa", "Teodor Herzel", 123, 12);
    final FineDto FINEDTO = new FineDto(123, OwnerStatus.FREE_PARKING, ADDRESS, "0534111222","1234@gmail.com",
            124);
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    InputDestination producer;

    @Autowired
    OutputDestination consumer;

    @Test
    void sendFineTest() throws JsonProcessingException {

        String message = objectMapper.writeValueAsString(FINEDTO);

//        String m = objectMapper.writeValueAsString(ADDRESS);
        producer.send(new GenericMessage<String>(message));
        Message<byte[]> messageReceived = consumer.receive(0, "getFineData-out-0");


        assertNull(messageReceived);

    }

}
