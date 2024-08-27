package kr.wishtarot.mnspushapi.service;

import kr.wishtarot.mnspushapi.domain.PushHist;

import java.util.List;

public interface PushManageService {

    /**
     * 디바이스가 등록되어 있는지 확인합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param deviceId 디바이스 ID (Private Push의 경우 회원 ID)
     * @return 디바이스가 등록되어 있으면 'Y', 그렇지 않으면 'N'을 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String isDeviceRegistered(String deviceType, String deviceId, String custId) throws Exception;

    /**
     * 디바이스를 등록하거나 삭제합니다.
     *
     * @param mode 동작 모드 ('reg': 등록, 'del': 삭제)
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param deviceId 디바이스 ID (Private Push의 경우는 회원 ID)
     * @param custId 고객 ID (등록 시 필수)
     * @return 'SUCCESS' 또는 'FAIL' 문자열을 반환합니다.
     *         등록 또는 삭제 작업이 성공하면 'SUCCESS'를 반환하고, 실패하면 'FAIL'을 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String manageDevice(String mode, String deviceType, String deviceId, String custId) throws Exception;

    String updateCustIdByDeviceId(String deviceId, String custId) throws Exception;

    String regDefaultNotification(String deviceId, String appCode) throws Exception;

    String regDefaultMarketingNotification(String deviceId, String appCode) throws Exception;

    String processAfterLogin(String deviceId, String custId, String appCode) throws Exception;

    String processAfterLogout(String deviceId, String custId, String appCode) throws Exception;

    /**
     * 알림 수신을 등록하거나 삭제합니다.
     *
     * @param mode 동작 모드 ('reg': 등록, 'del': 삭제)
     * @param appCode 앱 코드 (패키지 이름)
     * @param notiCode 알림 코드
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param deviceId 디바이스 ID (Private Push의 경우는 회원 ID)
     * @return 'SUCCESS' 또는 'FAIL' 문자열을 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String managePushNotification(String mode, String appCode, String notiCode, String deviceType, String deviceId) throws Exception;


    public List<PushHist> getPushHistWithAssociations(
            String deviceType,
            String deviceId,
            String custId,
            String appCode,
            String notiCode,
            String sendSuccessYn,
            String sendDt) throws Exception;


    /**
     * 앱에 등록된 Push 알림 목록을 JSON 형식으로 조회합니다.
     *
     * @param appCode 앱 코드 (패키지 이름)
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param deviceId 디바이스 ID (Private Push의 경우 회원 ID)
     * @return Push 알림 목록을 JSON 형식으로 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String getPushNotiListAsJson(String appCode, String deviceType, String deviceId) throws Exception;


    /**
     * Push 이력의 ReceiveSuccess 상태를 Y로 업데이트합니다.
     *
     * @param histSeq 수신된 메시지의 prefix에 포함되었던 HIST_SEQ 값
     * @return 성공 시 [SUCCESS], 실패 시 [FAIL]을 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String updateReceiveSuccessStatus(long histSeq) throws Exception;

}
