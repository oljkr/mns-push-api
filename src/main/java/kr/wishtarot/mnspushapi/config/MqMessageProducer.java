package kr.wishtarot.mnspushapi.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MqMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MqMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPushNotification(String queueName, String pushInfoNo, String targetUser, String title, String body, String targetPage, String messageId) {
        // 메시지에 포함될 데이터를 Map에 저장
        Map<String, Object> message = Map.of(
                "pushInfoNo", pushInfoNo,
                "targetUser", targetUser,
                "title", title,
                "body", body,
                "targetPage", targetPage,
                "messageId", messageId
        );

        // 메시지를 JSON 형식으로 변환하여 RabbitMQ로 전송
        rabbitTemplate.convertAndSend(queueName, message);
    }

}
