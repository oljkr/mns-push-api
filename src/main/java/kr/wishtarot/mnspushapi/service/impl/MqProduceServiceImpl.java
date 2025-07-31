package kr.wishtarot.mnspushapi.service.impl;

import kr.wishtarot.mnspushapi.config.MqMessageProducer;
import kr.wishtarot.mnspushapi.config.RabbitMQConfig;
import kr.wishtarot.mnspushapi.domain.MqMessage;
import kr.wishtarot.mnspushapi.service.MqProduceService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MqProduceServiceImpl implements MqProduceService {

    private final MqMessageProducer mqMessageProducer;

    @Autowired
    public MqProduceServiceImpl(MqMessageProducer mqMessageProducer) {
        this.mqMessageProducer = mqMessageProducer;
    }

    public String processAndSendPushNotification(MqMessage request) throws Exception {
        // 비즈니스 로직이 여기에 포함될 수 있습니다.
        // 예를 들어, 데이터 검증, 로깅, 기타 처리 등을 여기서 수행할 수 있습니다.

        // 검증 예시 (간단한 검증 로직)
        if (request.getTitle() == null || request.getBody() == null) {
            throw new IllegalArgumentException("Title and Body cannot be null");
        }

        // queueName을 요청에 따라 설정
        String queueName;
        if ("admin".equalsIgnoreCase(request.getQueueName())) {
            queueName = RabbitMQConfig.TAROT_ADMIN_QUEUE;
        } else if ("auto".equalsIgnoreCase(request.getQueueName())) {
            queueName = RabbitMQConfig.TAROT_AUTO_QUEUE;
        } else {
            throw new IllegalArgumentException("Invalid queue name: " + request.getQueueName());
        }

        // MQ로 푸쉬 알림 데이터 전송
        mqMessageProducer.sendPushNotification(
                queueName,
                request.getPushInfoNo(),
                request.getTargetUser(),
                request.getTitle(),
                request.getBody(),
                request.getTargetPage(),
                request.getMessageId()
        );

        return "[SUCCESS]";
    }

}

