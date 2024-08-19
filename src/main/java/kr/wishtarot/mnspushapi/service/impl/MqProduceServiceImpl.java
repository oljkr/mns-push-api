package kr.wishtarot.mnspushapi.service.impl;

import kr.wishtarot.mnspushapi.config.RabbitMQConfig;
import kr.wishtarot.mnspushapi.domain.MqMessage;
import kr.wishtarot.mnspushapi.service.MqProduceService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MqProduceServiceImpl implements MqProduceService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MqProduceServiceImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public String processCommand(String command, String jsonData) throws Exception {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("처리할 COMMAND가 없습니다.");
        } else if ("PUB_PUSH".equals(command)) {
            if (jsonData == null || jsonData.isEmpty()) {
                throw new IllegalArgumentException("There is no Data");
            }
            return handleSendAlarmMessage(jsonData);
        } else {
            throw new IllegalArgumentException("처리할 Command가 없습니다");
        }
    }

    public String handleSendAlarmMessage(String jsonData) throws Exception {
        // JSON 데이터를 POJO로 변환
        MqMessage message = objectMapper.readValue(jsonData, MqMessage.class);

        // 예외 처리 및 검증
        if (message.getNotiCode() == null || message.getContent() == null) {
            throw new IllegalArgumentException("Invalid data: notiCode or content is missing");
        }

//        // 비즈니스 로직을 통해 큐 이름 결정
//        String queueName;
//       if (/* some condition for admin */) {
//           queueName = RabbitMQConfig.ADMIN_QUEUE;
//       } else if (/* some condition for service */) {
//           queueName =  RabbitMQConfig.SERVICE_QUEUE;
//       } else {
//           throw new IllegalArgumentException("Invalid data: notiCode or content is missing");
//         }
//
//        produceToRabbitMQ(queueName, message);

        return "SUCC";
    }

    private void produceToRabbitMQ(String queueName, MqMessage message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

}

