package validation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import validation.dto.CameraRecordDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


//@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = TestChannelBinderConfiguration.class)
@Import(TestChannelBinderConfiguration.class)
class ValidationServiceTest {

    static LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(5);
    private static final Map<Integer, CameraRecordDto> cameraRecordsMap = Map.of(
            1, new CameraRecordDto(1, "KA12345", localDateTime),
            2, new CameraRecordDto(2, "KA12346", localDateTime),
            3, new CameraRecordDto(3, "KA12347", localDateTime)
    );

    @Autowired
    InputDestination producer;
    @Autowired
    OutputDestination consumer;


    @Test
    void validateParkingDataTest() throws IOException {

        producer.send(new GenericMessage<CameraRecordDto>(cameraRecordsMap.get(1)), "validateParkingData-in-0");

        Message<byte[]> receivedMessage = consumer.receive(0, "validateParkingData-out-0");

        assertThat(receivedMessage).isNotNull();
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals(cameraRecordsMap.get(1), objectMapper.readValue(receivedMessage.getPayload(), CameraRecordDto.class));

    }

}