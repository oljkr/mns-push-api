package kr.wishtarot.mnspushapi.service;

public interface MqProduceService {

    /**
     * Command를 처리하는 메서드.
     *
     * @param command 명령어 문자열
     * @param jsonData JSON 형식의 알림 데이터
     * @return 처리 결과 메시지
     * @throws Exception 처리 중 발생한 예외
     */
    String processCommand(String command, String jsonData) throws Exception;

    /**
     * 알림 메시지를 처리하는 메서드.
     *
     * @param jsonData JSON 형식의 알림 데이터
     * @return 처리 결과 메시지
     * @throws Exception 처리 중 발생한 예외
     */
    String handleSendAlarmMessage(String jsonData) throws Exception;
}
