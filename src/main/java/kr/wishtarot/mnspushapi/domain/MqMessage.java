package kr.wishtarot.mnspushapi.domain;

import lombok.Data;

@Data
public class MqMessage {  // 이전의 MessageBean 클래스
    private String notiCode;
    private String writerId;
    private String[] desIds;
    private String title;
    private String content;
    private String contentUrl;
    private String preTitle;
    private String preTitleUrl;
    private String source;
    private String sourceUrl;
    private String senderName;
}
