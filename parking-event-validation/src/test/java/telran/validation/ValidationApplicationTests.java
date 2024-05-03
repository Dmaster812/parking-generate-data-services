package telran.validation;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import telran.validation.dto.CameraRecordDto;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)

class ValidationApplicationTests {

    static final LocalDateTime PARKING_TIME = LocalDateTime.now().minusMinutes(50);
    static final CameraRecordDto NORMAL_CAR_RECORD = new CameraRecordDto(123, "IL 112-34-511", PARKING_TIME);
    static final CameraRecordDto NORMAL_NULL_RECORD = new CameraRecordDto(123, null, PARKING_TIME);
    static final CameraRecordDto BAD_RECORD = new CameraRecordDto(null, "IL 112-34-511", PARKING_TIME);

    @Autowired
    InputDestination producer;
    @Autowired
    OutputDestination consumer;

    String consumerBindingName = "validateParkingData-in-0";
    String producerBindingName = "valid-out-0";
    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void NormalRecordTest() throws Exception {

        objectMapper.registerModule(new JavaTimeModule());
        String sendingMessage = objectMapper.writeValueAsString(NORMAL_CAR_RECORD);

        producer.send(new GenericMessage<String>(sendingMessage), consumerBindingName);
        Message<byte[]> message = consumer.receive(0, producerBindingName);
        assertNotNull(message);
    }

    @Test
    void BadRecordTest() throws Exception {

        objectMapper.registerModule(new JavaTimeModule());
        String sendingMessage = objectMapper.writeValueAsString(BAD_RECORD);

        producer.send(new GenericMessage<String>(sendingMessage), consumerBindingName);
        Message<byte[]> message = consumer.receive(0, producerBindingName);
        assertNull(message);
    }

    @Test
    void NullRecordTest() throws Exception {

        objectMapper.registerModule(new JavaTimeModule());
        String sendingMessage = objectMapper.writeValueAsString(NORMAL_NULL_RECORD);

        producer.send(new GenericMessage<String>(sendingMessage), consumerBindingName);
        Message<byte[]> message = consumer.receive(0, producerBindingName);
        assertNotNull(message);
    }

}
