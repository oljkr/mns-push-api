package kr.wishtarot.mnspushapi.service;

public interface PushManageService {

    /**
     * 디바이스가 등록되어 있는지 확인합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param deviceId 디바이스 ID (Private Push의 경우 회원 ID)
     * @param appCode (선택적) 앱 코드 (패키지 이름)
     * @return 디바이스가 등록되어 있으면 'Y', 그렇지 않으면 'N'을 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String isDeviceRegistered(String deviceType, String deviceId, String appCode) throws Exception;

    /**
     * 디바이스를 등록하거나 삭제합니다.
     *
     * @param mode 동작 모드 ('reg': 등록, 'del': 삭제)
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param deviceId 디바이스 ID (Private Push의 경우는 회원 ID)
     * @param appCode 앱 코드 (패키지 이름)
     * @param custId 고객 ID (등록 시 필수)
     * @return 'SUCCESS' 또는 'FAIL' 문자열을 반환합니다.
     *         등록 또는 삭제 작업이 성공하면 'SUCCESS'를 반환하고, 실패하면 'FAIL'을 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String manageDevice(String mode, String deviceType, String deviceId, String appCode, String custId) throws Exception;

    // Optional: If handleDeviceRegistration and handleDeviceDeletion need to be exposed publicly
    // String handleDeviceRegistration(PushDeviceVO pushDeviceVO) throws Exception;
    // String handleDeviceDeletion(PushDeviceVO pushDeviceVO) throws Exception;

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

    /**
     * 전송된 Push 알림 이력 목록을 JSON 형식으로 조회합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param deviceId 디바이스 ID (Private Push의 경우 회원 ID)
     * @param appCode (선택적) 앱 코드 (패키지 이름)
     * @param receiveSuccesYn (선택적) 전송 성공 여부 ('Y' or 'N')
     * @param qryStartDt (선택적) 요청 날짜부터 최근일까지의 데이터 조회 (형식: LocalDateTime)
     * @return 알림 이력 목록을 JSON 형식으로 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String getPushHistListAsJson(String deviceType, String deviceId, String appCode, String receiveSuccesYn, String qryStartDt) throws Exception;

    /**
     * 단말기로부터 Feedback이 되지 않은 Push 알림 이력(재전송 대상) 목록을 JSON 형식으로 조회합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param appCode (선택적) 앱 코드 (패키지 이름)
     * @param notiCode (선택적) 알림 코드
     * @param custId (선택적) 회원 ID
     * @param deviceId (선택적) 디바이스 ID (Private Push의 경우 회원 ID)
     * @param qryStartDt (선택적) 요청 날짜부터 최근일까지의 데이터 조회 (형식: yyyyMMdd)
     * @return 알림 이력 목록을 JSON 형식으로 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String getTargetResendListAsJson(String deviceType, String appCode, String notiCode, String custId, String deviceId, String qryStartDt) throws Exception;

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
     * 회원ID에 등록된 디바이스ID 목록을 JSON 형식으로 조회합니다.
     *
     * @param appCode 앱 코드 (패키지 이름)
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param custId 회원 ID
     * @return 디바이스 ID 목록을 JSON 형식으로 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String getPushDeviceListAsJson(String appCode, String deviceType, String custId) throws Exception;

    /**
     * 특정 알림 코드가 등록된 회원ID 목록을 JSON 형식으로 조회합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param appCode 앱 코드 (패키지 이름)
     * @param notiCode 알림 코드
     * @return 회원 ID 목록을 JSON 형식으로 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String getCustIdListAsJson(String deviceType, String appCode, String notiCode) throws Exception;

    /**
     * 디바이스 ID를 수정합니다.
     *
     * @param appCode 앱 코드 (패키지 이름)
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS, '99': Private Push)
     * @param oldDeviceId 기존 디바이스 ID
     * @param newDeviceId 변경할 디바이스 ID
     * @return 성공 시 [SUCCESS], 실패 시 [FAIL]을 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String updateDeviceIds(String appCode, String deviceType, String oldDeviceId, String newDeviceId) throws Exception;

    /**
     * Push 이력의 ReceiveSuccess 상태를 Y로 업데이트합니다.
     *
     * @param histSeq 수신된 메시지의 prefix에 포함되었던 HIST_SEQ 값
     * @return 성공 시 [SUCCESS], 실패 시 [FAIL]을 반환합니다.
     * @throws Exception 예외 발생 시 예외를 던집니다.
     */
    String updateReceiveSuccessStatus(long histSeq) throws Exception;
}
