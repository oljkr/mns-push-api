package kr.wishtarot.mnspushapi.web.controller;

import kr.wishtarot.mnspushapi.domain.PushDevice;
import kr.wishtarot.mnspushapi.domain.PushHist;
import kr.wishtarot.mnspushapi.service.impl.PushManageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param custId 회원 ID
     * @return 디바이스가 등록되어 있으면 'Y', 그렇지 않으면 'N'을 반환합니다.
     *         오류가 발생할 경우 HTTP 500 상태와 에러 메시지를 반환합니다.
     */
    @GetMapping("/device/registration-status")
    public ResponseEntity<String> isDeviceRegistered(@RequestParam String deviceType,
                                                     @RequestParam String deviceId,
                                                     @RequestParam(required = false) String custId) {
        try {
            return ResponseEntity.ok(pushManageService.isDeviceRegistered(deviceType, deviceId, custId));
        } catch (Exception e) {
            return handleException(e, "Error in isDeviceRegistered");
        }
    }


    /**
     * 디바이스를 등록(또는 업데이트)하거나 삭제하는 요청을 처리합니다.
     *
     * @param mode 'reg' 등록, 'del' 삭제
     * @param deviceType 디바이스 타입 ('01': 안드로이드, '02': iOS)
     * @param deviceId 디바이스 ID
     * @param custId 고객 ID (등록 시 필수)
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/device/manage")
    public ResponseEntity<String> manageDevice(@RequestParam String mode,
                                               @RequestParam String deviceType,
                                               @RequestParam String deviceId,
                                               @RequestParam(required = false) String custId) {
        try {
            return ResponseEntity.ok(pushManageService.manageDevice(mode, deviceType, deviceId, custId));
        } catch (Exception e) {
            return handleException(e, "Error in manageDevice");
        }
    }

    /**
     * 디바이스 정보에 고객 id를 등록한다.
     *
     * @param deviceId 디바이스 ID
     * @param custId 고객 ID
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/device/update-custid")
    public ResponseEntity<String> updateCustIdByDeviceId(@RequestParam String deviceId,
                                                    @RequestParam String custId){
        try {
            return ResponseEntity.ok(pushManageService.updateCustIdByDeviceId(deviceId, custId));
        } catch (Exception e) {
            return handleException(e, "Error in updateCustid");
        }
    }

    /**
     * 디바이스 정보에 고객 id를 삭제한다.
     *
     * @param deviceId 디바이스 ID
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/device/delete-custid")
    public ResponseEntity<String> deleteCustIdByDeviceId(@RequestParam String deviceId){
        try {
            return ResponseEntity.ok(pushManageService.deleteCustIdByDeviceId(deviceId));
        } catch (Exception e) {
            return handleException(e, "Error in deleteCustid");
        }
    }

    /**
     * 기기에서 알림 허용 후 비로그인 상태에도 받을 수 있는 알림을 설정하는 요청을 처리합니다.
     *
     * @param deviceId 디바이스 ID
     * @param appCode 애플리케이션 코드
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/notifications/reg-default")
    public ResponseEntity<String> regDefaultNotification(@RequestParam String deviceId,
                                                    @RequestParam String appCode){
        try {
            return ResponseEntity.ok(pushManageService.regDefaultNotification(deviceId, appCode));
        } catch (Exception e) {
            return handleException(e, "Error in regDefaultNotification");
        }
    }

    /**
     * 기기에서 알림 허용 후 비로그인 상태에서 마케팅용 알림을 설정하는 요청을 처리합니다.
     *
     * @param deviceId 디바이스 ID
     * @param appCode 애플리케이션 코드
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/notifications/reg-default-marketing")
    public ResponseEntity<String> regDefaultMarketingNotification(@RequestParam String deviceId,
                                                         @RequestParam String appCode){
        try {
            return ResponseEntity.ok(pushManageService.regDefaultMarketingNotification(deviceId, appCode));
        } catch (Exception e) {
            return handleException(e, "Error in regDefaultMarketingNotification");
        }
    }


    /**
     * 등록된 모든 알림 목록을 조회합니다.
     *
     * @return HTTP 응답 상태와 결과 메시지
     */
    @GetMapping("/notifications/noti-consent-info")
    public ResponseEntity<?> getNotiConsentInfoList(@RequestParam String deviceId){
        try {
            Map<String, String> response = pushManageService.getNotiConsentInfoList(deviceId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleJsonException(e, "Error in getNotiConsentInfoList");
        }
    }

    /**
     * 로그인 후 디바이스 정보에 고객 id를 등록하고 해당 디바이스에 대한 알림을 설정하는 요청을 처리합니다.
     *
     * @param deviceId 디바이스 ID
     * @param custId 고객 ID
     * @param appCode 애플리케이션 코드
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/auth/after-login")
    public ResponseEntity<String> processAfterLogin(@RequestParam String deviceId,
                                                    @RequestParam String custId,
                                                    @RequestParam String appCode){
        try {
            return ResponseEntity.ok(pushManageService.processAfterLogin(deviceId, custId, appCode));
        } catch (Exception e) {
            return handleException(e, "Error in manageDevice");
        }
    }

    /**
     * 로그아웃 후 디바이스 정보에 고객 id를 삭제하고 해당 디바이스에 대한 알림을 삭제하는 요청을 처리합니다.
     *
     * @param deviceId 디바이스 ID
     * @param custId 고객 ID
     * @param appCode 애플리케이션 코드
     * @return HTTP 응답 상태와 결과 메시지
     */
    @PostMapping("/auth/after-logout")
    public ResponseEntity<String> processAfterLogout(@RequestParam String deviceId,
                                                    @RequestParam String custId,
                                                    @RequestParam String appCode){
        try {
            return ResponseEntity.ok(pushManageService.processAfterLogout(deviceId, custId, appCode));
        } catch (Exception e) {
            return handleException(e, "Error in manageDevice");
        }
    }

    /**
     * 전송된 Push 알림 이력 목록을 조회합니다.
     *
     *
     */
    @GetMapping("/notifications/history")
    public ResponseEntity<?> getPushHistory(
            @RequestParam(value = "deviceType", required = false) String deviceType,
            @RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "custId", required = false) String custId,
            @RequestParam(value = "appCode", required = false) String appCode,
            @RequestParam(value = "notiCode", required = false) String notiCode,
            @RequestParam(value = "sendSuccessYn", required = false) String sendSuccessYn,
            @RequestParam(value = "sendDt", required = false) String sendDt) {

        try {
            List<PushHist> pushHistList = pushManageService.getPushHistWithAssociations(
                    deviceType, deviceId, custId, appCode, notiCode, sendSuccessYn, sendDt);
            return ResponseEntity.ok(pushHistList);
        } catch (Exception e) {
            return handleJsonException(e, "Error in getPushHistory");
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
     * 등록된 모든 알림 목록을 조회합니다.
     *
     * @return HTTP 응답 상태와 결과 메시지
     */
    @GetMapping("/notifications/push-list")
    public ResponseEntity<String> getAppPushInfoList(){
        try {
            return ResponseEntity.ok(pushManageService.getAppPushInfoList());
        } catch (Exception e) {
            return handleException(e, "Error in getAppPushInfoList");
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

    // JSON 형식으로 예외 처리 메서드
    private ResponseEntity<Map<String, Object>> handleJsonException(Exception e, String errorMessage) {
        logger.error("{}: {}", errorMessage, e.getMessage(), e);

        // JSON 응답을 위한 Map 생성
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", errorMessage + ": " + e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}


