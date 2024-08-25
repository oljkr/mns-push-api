package kr.wishtarot.mnspushapi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.MessageDeliveryMode;

@Configuration
public class RabbitMQConfig {

    public static final String ADMIN_QUEUE = "adminQueue";
    public static final String SERVICE_QUEUE = "serviceQueue";

    @Bean
    public Queue adminQueue() {
        // 큐를 내구성 있게 설정 (durable: true)
        return new Queue(ADMIN_QUEUE, true);
    }

    @Bean
    public Queue serviceQueue() {
        // 큐를 내구성 있게 설정 (durable: true)
        return new Queue(SERVICE_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        // 메시지를 영속성 있게 처리하도록 설정
        rabbitTemplate.setBeforePublishPostProcessors(message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });

        return rabbitTemplate;
    }
}
