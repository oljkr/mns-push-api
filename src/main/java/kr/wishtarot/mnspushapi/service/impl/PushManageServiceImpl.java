package kr.wishtarot.mnspushapi.service.impl;

import kr.wishtarot.mnspushapi.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import kr.wishtarot.mnspushapi.dao.PushManageDAO;
import kr.wishtarot.mnspushapi.service.PushManageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class PushManageServiceImpl implements PushManageService {

    private final PushManageDAO pushManageDAO;
    private final ObjectMapper objectMapper;

    private static final String CHANNEL_ALARM = "FF001ch1";
    private static final String MNS_LOGGER_NAME = "MNS";

    @Autowired
    public PushManageServiceImpl(PushManageDAO pushManageDAO, ObjectMapper objectMapper) {
        this.pushManageDAO = pushManageDAO;
        this.objectMapper = objectMapper;
    }

    @Override
    public String isDeviceRegistered(String deviceType, String deviceId, String custId) throws Exception {
        int count = countPushDeviceByCriteria(deviceType, deviceId, custId);
        return (count > 0) ? "Y" : "N";
    }

    private Long getPdNoFromPushDevice(String deviceType, String deviceId, String custId) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, custId, null);
        return pushManageDAO.getPdNoFromPushDevice(pushDevice);
    }

    private List<Long> getPdNoListFromPushDevice(String deviceType, String deviceId, String custId) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, custId, null);
        return pushManageDAO.getPdNoListFromPushDevice(pushDevice);
    }

    private int countPushDeviceByCriteria(String deviceType, String deviceId, String custId) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, custId, null);
        return pushManageDAO.countPushDeviceByCriteria(pushDevice);
    }

    @Override
    public String manageDevice(String mode, String deviceType, String deviceId, String custId) throws Exception {
        custId = normalize(custId);
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, custId, null);

        if ("reg".equalsIgnoreCase(mode)) {
            return handleDeviceRegistration(pushDevice);
        } else if ("del".equalsIgnoreCase(mode)) {
            return handleDeviceDeletion(pushDevice);
        } else {
            throw new IllegalArgumentException("[FAIL] Invalid mode");
        }
    }

    @Override
    @Transactional
    public String processAfterLogin(String deviceId, String custId, String appCode) throws Exception {
        // 기기등록) 현재 토큰 값에 해당하는 데이터에 custId를 등록함
        PushDevice pushDevice = createPushDevice(null, deviceId, custId, null);
        pushManageDAO.updateCustIdByDeviceId(pushDevice);
        // 알림등록) 로그인한 회원이 받을 수 있는 알림을 추가함
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("custId", custId);
        params.put("app_code", appCode);

        pushManageDAO.insertPushNotiRegByDeviceAndCustIdAndAppCode(params);

        return "[SUCCESS]";
    }

    @Override
    @Transactional
    public String processAfterLogout(String deviceId, String custId, String appCode) throws Exception {
        // 알림삭제) 로그아웃한 회원이 받을 수 있는 알림을 삭제함
        // 해당 회원이 로그인했을 때 받는 pnr_no를 가져옴
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("custId", custId);
        params.put("app_code", appCode);

        pushManageDAO.insertPushNotiRegByDeviceAndCustIdAndAppCode(params);
        List<Long> pnrNoList = pushManageDAO.selectPnrNoByDeviceAndCustIdAndAppCode(params);

        // 2. pnr_no 리스트를 기반으로 해당 레코드 삭제
        if (!pnrNoList.isEmpty()) {
            Map<String, Object> deleteParams = new HashMap<>();
            deleteParams.put("pnrNoList", pnrNoList);
            pushManageDAO.deletePushNotiRegByPnrNoList(deleteParams);
        }

        // 기기삭제) 현재 토큰 값에 해당하는 custId를 삭제함
        PushDevice pushDevice = createPushDevice(null, deviceId, custId, null);
        pushManageDAO.updateCustIdToNull(pushDevice);

        return "[SUCCESS]";
    }

    private String handleDeviceRegistration(PushDevice pushDevice) throws Exception {
        int result = pushManageDAO.insertOrUpdatePushDevice(pushDevice);
        return (result > 0) ? "[SUCCESS]" : "[FAIL] Registration failed";
    }

    @Transactional
    private String handleDeviceDeletion(PushDevice pushDevice) throws Exception {
        // 등록된 알림 수신 내용을 먼저 삭제한다.
        pushManageDAO.deletePushNotiRegUsingPushDevice(pushDevice);
        pushManageDAO.deletePushDevice(pushDevice);
        return "[SUCCESS]";
    }

    private String updateCustIdByDeviceId(PushDevice pushDevice) throws Exception {
        int result = pushManageDAO.updateCustIdByDeviceId(pushDevice);
        return (result > 0) ? "[SUCCESS]" : "[FAIL] Update failed";
    }

    @Override
    public String managePushNotification(String mode, String appCode, String notiCode, String deviceType, String deviceId) throws Exception {
        // push_device 테이블에서 정보를 가져옴(pd_no가져오기)
        Long pdNo = getPdNoFromPushDevice(deviceType, deviceId,null);
        if(pdNo == null) {
            return "[FAIL] Device not registered";
        }

        Map<String, Object> params = new HashMap<>();
        params.put("appCode", appCode);
        params.put("notiCode", notiCode);

        // 해당하는 알림이 등록되어 있는지 확인
        // app_code와 noti_code로 push_info테이블과 app_info테이블을 조인해서 push_info_no를 가져옴
        Long push_info_no = pushManageDAO.getPushInfoNoByAppCodeAndNotiCode(params);
        if(push_info_no == null) {
            return "[FAIL] Notification not registered";
        }

        PushNotiReg pushNotiReg = createPushNotiReg(pdNo, push_info_no, null);

        int pushDeviceResult = 0;
        if ("reg".equalsIgnoreCase(mode)) {
            // push_info_no와 위의 pd_no로 알림을 등록되어 있는 지 확인
            pushDeviceResult = pushManageDAO.insertPushNotiReg(pushNotiReg);
            // push_info_no와 위의 pd_no로 알림을 등록함
            pushDeviceResult = pushManageDAO.insertPushNotiReg(pushNotiReg);
        } else if ("del".equalsIgnoreCase(mode)) {
            pushDeviceResult = pushManageDAO.deletePushNotiReg(pushNotiReg);
        } else {
            throw new IllegalArgumentException("[FAIL] Invalid mode");
        }

        return (pushDeviceResult > 0) ? "[SUCCESS]" : "[FAIL]";
    }

    @Override
    public List<PushHist> getPushHistWithAssociations(
            String deviceType,
            String deviceId,
            String custId,
            String appCode,
            String notiCode,
            String sendSuccessYn,
            String sendDt) throws Exception {

        LocalDateTime parsedSendDt = null;
        try {
            // sendDt가 null이 아니고 빈 문자열이 아닌 경우에만 파싱 시도
            if (sendDt != null && !sendDt.trim().isEmpty()) {
                parsedSendDt = LocalDateTime.parse(sendDt);
            }
        } catch (DateTimeParseException e) {
            // 예외가 발생하면 원하는 방식으로 예외 처리
            throw new IllegalArgumentException("Invalid date format for sendDt: " + sendDt, e);
        }

        // 이미 파싱한 parsedSendDt 사용
        PushHist criteria = PushHist.builder()
                .deviceType(deviceType)
                .deviceId(deviceId)
                .custId(custId)
                .appCode(appCode)
                .notiCode(notiCode)
                .sendSuccessYn(sendSuccessYn)
                .sendDt(parsedSendDt)  // 여기서는 sendDt를 다시 파싱하지 않음
                .build();

        return pushManageDAO.selectPushHistWithAssociations(criteria);
    }

    @Override
    public String getPushNotiListAsJson(String appCode, String deviceType, String deviceId) throws Exception {
//        PushDevice pushDevice = createPushDevice(deviceType, deviceId, appCode, null,null);
//        List<PushNotiInfo> pushNotiInfoList = pushManageDAO.getPushNotiInfoList(pushDevice);
//        return objectMapper.writeValueAsString(pushNotiInfoList);
        return null;
    }

    @Override
    public String updateReceiveSuccessStatus(long histSeq) throws Exception {
        int pushResult = pushManageDAO.updateReceiveSuccess(histSeq);
        return (pushResult > 0) ? "[SUCCESS]" : "[FAIL]";
    }

    // Utility methods for object creation and normalization

    private String normalize(String value) {
        return (value == null) ? "" : value.trim();
    }

    private PushDevice createPushDevice(String deviceType, String deviceId, String custId, LocalDateTime regDt) {
        return PushDevice.builder()
                .deviceType(deviceType)
                .deviceId(deviceId)
                .custId(custId)
                .regDt(regDt)
                .build();
    }

    private AppInfo createAppInfo(String appCode, String appNm) {
        return AppInfo.builder()
                .appCode(appCode)
                .appNm(appNm)
                .build();
    }

    private PushNotiReg createPushNotiReg(Long pdNo, Long pushInfoNo, LocalDateTime regDt) {
        return PushNotiReg.builder()
                .pdNo(pdNo)
                .pushInfoNo(pushInfoNo)
                .regDt(regDt)
                .build();
    }

    private PushInfo createPushInfo(Long pushAppNo, String notiCode, String notiNm) {
        return PushInfo.builder()
                .pushAppNo(pushAppNo)
                .notiCode(notiCode)
                .notiNm(notiNm)
                .build();
    }


    private PushNotiReg createPushNotiReg(Long pnrNo, Long pdNo, Long pushInfoNo, LocalDateTime regDt) {
        return PushNotiReg.builder()
                .pnrNo(pnrNo)
                .pdNo(pdNo)
                .pushInfoNo(pushInfoNo)
                .regDt(regDt)
                .build();
    }


    private TmpPushDevice createTmpPushDevice(String appCode, String deviceType, String oldDeviceId, String newDeviceId) {
        return TmpPushDevice.builder()
                .appCode(appCode)
                .deviceType(deviceType)
                .oldDeviceId(oldDeviceId)
                .newDeviceId(newDeviceId)
                .build();
    }

}
