package kr.wishtarot.mnspushapi.web.controller;

import kr.wishtarot.mnspushapi.service.impl.PushManageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/push-notifications")
public class PushManageController {

    private static final Logger logger = LoggerFactory.getLogger(PushManageController.class);

    private final PushManageServiceImpl pushManageService;

    @Autowired
    public PushManageController(PushManageServiceImpl pushManageService) {
        this.pushManageService = pushManageService;
    }

    /**
     * 디바이스가 등록되어 있는지 확인하는 요청을 처리합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param deviceId 디바이스 ID
     * @param appCode 앱 코드 (패키지 이름)
     * @return 디바이스가 등록되어 있으면 'Y', 그렇지 않으면 'N'을 반환합니다.
     *         오류가 발생할 경우 HTTP 500 상태와 에러 메시지를 반환합니다.
     */
    @GetMapping("/device/registration-status")
    public ResponseEntity<String> isDeviceRegistered(@RequestParam String deviceType,
                                                     @RequestParam String deviceId,
                                                     @RequestParam String appCode) {
        try {
            return ResponseEntity.ok(pushManageService.isDeviceRegistered(deviceType, deviceId, appCode));
        } catch (Exception e) {
            return handleException(e, "Error in isDeviceRegistered");
        }
    }

    /**
     * 디바이스를 등록하거나 삭제하는 요청을 처리합니다.
     *
     * @param mode 'reg' 등록, 'del' 삭제
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param deviceId 디바이스 ID
     * @param appCode 앱 코드 (패키지 이름)
     * @param custId 고객 ID (등록 시 필수)
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/device/manage")
    public ResponseEntity<String> manageDevice(@RequestParam String mode,
                                               @RequestParam String deviceType,
                                               @RequestParam String deviceId,
                                               @RequestParam String appCode,
                                               @RequestParam(required = false) String custId) {
        try {
            return ResponseEntity.ok(pushManageService.manageDevice(mode, deviceType, deviceId, appCode, custId));
        } catch (Exception e) {
            return handleException(e, "Error in manageDevice");
        }
    }

    /**
     * 알림 수신을 등록하거나 삭제합니다.
     *
     * @param mode 동작 모드 ('reg': 등록, 'del': 삭제)
     * @param appCode 앱 코드 (패키지 이름)
     * @param notiCode 알림 코드
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param deviceId 디바이스 ID
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/notifications/manage")
    public ResponseEntity<String> managePushNotification(@RequestParam String mode,
                                                         @RequestParam String appCode,
                                                         @RequestParam String notiCode,
                                                         @RequestParam String deviceType,
                                                         @RequestParam String deviceId) {
        try {
            return ResponseEntity.ok(pushManageService.managePushNotification(mode, appCode, notiCode, deviceType, deviceId));
        } catch (Exception e) {
            return handleException(e, "Error in managePushNotification");
        }
    }

    /**
     * 전송된 Push 알림 이력 목록을 조회합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param deviceId 디바이스 ID
     * @param appCode (선택적) 앱 코드 (패키지 이름)
     * @param receiveSuccesYn (선택적) 전송 성공 여부 ('Y' or 'N')
     * @param qryStartDt (선택적) 요청 날짜부터 최근일까지의 데이터 조회 (형식: YYYYMMDD)
     * @return 알림 이력 목록을 JSON 형식으로 반환합니다.
     */
    @GetMapping("/notifications/history")
    public ResponseEntity<String> getPushHistory(@RequestParam String deviceType,
                                                 @RequestParam String deviceId,
                                                 @RequestParam(required = false) String appCode,
                                                 @RequestParam(required = false) String receiveSuccesYn,
                                                 @RequestParam(required = false) String qryStartDt) {
        try {
//            return ResponseEntity.ok(pushManageService.getPushHistListAsJson(deviceType, deviceId, appCode, receiveSuccesYn, qryStartDt));
            return null;
        } catch (Exception e) {
            return handleException(e, "Error in getPushHistory");
        }
    }

    /**
     * 단말기로부터 Feedback이 되지 않은 Push 알림 이력(재전송 대상) 목록을 조회합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param appCode (선택적) 앱 코드 (패키지 이름)
     * @param notiCode (선택적) 알림 코드
     * @param custId (선택적) 회원 ID
     * @param deviceId (선택적) 디바이스 ID
     * @param qryStartDt (선택적) 요청 날짜부터 최근일까지의 데이터 조회 (형식: yyyyMMdd)
     * @return 알림 이력 목록을 JSON 형식으로 반환합니다.
     */
    @GetMapping("/notifications/resend-targets")
    public ResponseEntity<String> getTargetResendList(@RequestParam String deviceType,
                                                      @RequestParam(required = false) String appCode,
                                                      @RequestParam(required = false) String notiCode,
                                                      @RequestParam(required = false) String custId,
                                                      @RequestParam(required = false) String deviceId,
                                                      @RequestParam(required = false) String qryStartDt) {
        try {
//            return ResponseEntity.ok(pushManageService.getTargetResendListAsJson(deviceType, appCode, notiCode, custId, deviceId, qryStartDt));
            return null;
        } catch (Exception e) {
            return handleException(e, "Error in getTargetResendList");
        }
    }

    /**
     * 사용자의 기기와 앱으로 등록된 Push 알림 목록을 조회합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param appCode 앱 코드 (패키지 이름)
     * @param deviceId 디바이스 ID
     * @return Push 알림 목록을 JSON 형식으로 반환합니다.
     */
    @GetMapping("/notifications/list")
    public ResponseEntity<String> getPushNotificationList(@RequestParam String appCode,
                                                          @RequestParam String deviceType,
                                                          @RequestParam String deviceId) {
        try {
            return ResponseEntity.ok(pushManageService.getPushNotiListAsJson(appCode, deviceType, deviceId));
        } catch (Exception e) {
            return handleException(e, "Error in getPushNotificationList");
        }
    }

    /**
     * 회원ID에 등록된 디바이스ID 목록을 조회합니다.
     *
     * @param appCode 앱 코드 (패키지 이름)
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param custId 회원 ID
     * @return 디바이스 ID 목록을 JSON 형식으로 반환합니다.
     */
    @GetMapping("/device/list")
    public ResponseEntity<String> getDeviceList(@RequestParam String appCode,
                                                @RequestParam String deviceType,
                                                @RequestParam String custId) {
        try {
//            return ResponseEntity.ok(pushManageService.getPushDeviceListAsJson(appCode, deviceType, custId));
            return null;
        } catch (Exception e) {
            return handleException(e, "Error in getDeviceList");
        }
    }

    /**
     * 특정 알림 코드가 등록된 회원ID 목록을 조회합니다.
     *
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param appCode 앱 코드 (패키지 이름)
     * @param notiCode 알림 코드
     * @return 회원 ID 목록을 JSON 형식으로 반환합니다.
     */
    @GetMapping("/customer/list")
    public ResponseEntity<String> getCustomerList(@RequestParam String deviceType,
                                                  @RequestParam String appCode,
                                                  @RequestParam String notiCode) {
        try {
            return ResponseEntity.ok(pushManageService.getCustIdListAsJson(deviceType, appCode, notiCode));
        } catch (Exception e) {
            return handleException(e, "Error in getCustomerList");
        }
    }

    /**
     * 디바이스 ID를 수정합니다.
     *
     * @param appCode 앱 코드 (패키지 이름)
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param oldDeviceId 기존 디바이스 ID
     * @param newDeviceId 변경할 디바이스 ID
     * @return 성공 시 [SUCCESS], 실패 시 [FAIL]을 반환합니다.
     */
    @PostMapping("/device/update")
    public ResponseEntity<String> updateDevice(@RequestParam String appCode,
                                               @RequestParam String deviceType,
                                               @RequestParam String oldDeviceId,
                                               @RequestParam String newDeviceId) {
        try {
            return ResponseEntity.ok(pushManageService.updateDeviceIds(appCode, deviceType, oldDeviceId, newDeviceId));
        } catch (Exception e) {
            return handleException(e, "Error in updateDevice");
        }
    }

    /**
     * Push 이력의 ReceiveSuccess를 Y로 업데이트합니다(단말기로부터 Feedback).
     *
     * @param histSeq 수신된 메시지의 prefix에 포함되었던 HIST_SEQ 값
     * @return 성공 시 [SUCCESS], 실패 시 [FAIL]을 반환합니다.
     */
    @PostMapping("/notifications/update-receive-success")
    public ResponseEntity<String> updateReceiveSuccess(@RequestParam long histSeq) {
        try {
            return ResponseEntity.ok(pushManageService.updateReceiveSuccessStatus(histSeq));
        } catch (Exception e) {
            return handleException(e, "Error in updateReceiveSuccess");
        }
    }

    private ResponseEntity<String> handleException(Exception e, String errorMessage) {
        logger.error("{}: {}", errorMessage, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[FAIL] " + errorMessage + ": " + e.getMessage());
    }

}
