package kr.wishtarot.mnspushapi.domain;

import lombok.Data;

@Data
public class MqMessage {  // 이전의 MessageBean 클래스
    private String queueName;
    private String pushInfoNo;
    private String targetUser;
    private String title;
    private String body;
    private String targetPage;
    private String messageId;
}
