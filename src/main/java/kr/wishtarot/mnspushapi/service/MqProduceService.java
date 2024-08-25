package kr.wishtarot.mnspushapi.service;

import kr.wishtarot.mnspushapi.domain.MqMessage;

public interface MqProduceService {

    String processAndSendPushNotification(MqMessage request) throws Exception;
}
