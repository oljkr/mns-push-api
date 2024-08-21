package kr.wishtarot.mnspushapi.service.impl;

import kr.wishtarot.mnspushapi.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import kr.wishtarot.mnspushapi.dao.PushManageDAO;
import kr.wishtarot.mnspushapi.service.PushManageService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public String isDeviceRegistered(String deviceType, String deviceId) throws Exception {
        int count = countPushDeviceByCriteria(deviceType, deviceId);
        return (count > 0) ? "Y" : "N";
    }

    private String getCustIdFromPushDevice(String deviceType, String deviceId, String appCode) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, null, null);
        return pushManageDAO.getCustIdFromPushDevice(pushDevice);
    }

    private Long getPdNoFromPushDevice(String deviceType, String deviceId, String custId) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, custId, null);
        return pushManageDAO.getPdNoFromPushDevice(pushDevice);
    }

    private List<Long> getPdNoListFromPushDevice(String deviceType, String deviceId, String custId) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, custId, null);
        return pushManageDAO.getPdNoListFromPushDevice(pushDevice);
    }

    private int countPushDeviceByCriteria(String deviceType, String deviceId) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, null, null);
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

    private String handleDeviceRegistration(PushDevice pushDevice) throws Exception {
        int result = pushManageDAO.insertPushDevice(pushDevice);
        return (result > 0) ? "[SUCCESS]" : "[FAIL] Registration failed";

    }

    private String handleDeviceDeletion(PushDevice pushDevice) throws Exception {
        // 등록된 알림 수신 내용을 먼저 삭제한다.
        pushManageDAO.deletePushNotiRegUsingPushDevice(pushDevice);
        pushManageDAO.deletePushDevice(pushDevice);

        return "[SUCCESS]";
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
            // push_info_no와 위의 pd_no로 알림을 등록함
            pushDeviceResult = pushManageDAO.insertPushNotiReg(pushNotiReg);
        } else if ("del".equalsIgnoreCase(mode)) {
            pushDeviceResult = pushManageDAO.deletePushNotiReg(pushNotiReg);
        } else {
            throw new IllegalArgumentException("[FAIL] Invalid mode");
        }

        return (pushDeviceResult > 0) ? "[SUCCESS]" : "[FAIL]";
    }


//    @Override
//    public String getPushHistListAsJson(String deviceType, String deviceId, String appCode, String receiveSuccesYn, String qryStartDt) throws Exception {
////        PushHist pushHist = createPushHist(deviceType, deviceId, appCode, receiveSuccesYn, qryStartDt);
////        List<PushHist> pushHistList = pushManageDAO.getPushHistList(pushHist);
////        return objectMapper.writeValueAsString(pushHistList);
//        return null;
//    }

    @Override
    public String getPushHistListAsJson(String deviceType, String deviceId, String custId, String appCode, String notiCode, String sendSuccesYn, String qryStartDt) throws Exception {
        //만약 deviceType, deviceId, custId의 값이 있다면 이걸로 push_device테이블의 pd_no를 가져옴
//        List<Long> pdNoList = getPdNoListFromPushDevice(deviceType, deviceId, custId);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("appCode", appCode);
//        params.put("notiCode", notiCode);
//
//        // 해당하는 알림이 등록되어 있는지 확인
//        // app_code와 noti_code로 push_info테이블과 app_info테이블을 조인해서 push_info_no를 가져옴
//        List<Long> pushInfoNoList = pushManageDAO.getPushInfoNoListByAppCodeAndNotiCode(params);
//        if (pushInfoNoList == null) {
//            return "[FAIL] Notification not registered";
//        }
//
//        List<Long> pnrNoList = null;
//
//        pushInfoNoList.forEach(pushInfoNo -> {
//            pdNoList.forEach(pdNo -> {
//                PushNotiReg pushNotiReg = createPushNotiReg(pdNo, push_info_no, null);
//
//                //pd_no와 push_info_no로 pnr_no 가져와서 pnrNoList 리스트에 추가
//
//
//            });
//        });
        
        //pnr_no로 push_hist테이블에서 조회
//        List<PushHist> pushHistList = pushManageDAO.getPushHistListByPnrNoList(pnrNoList, sendSuccesYn, qryStartDt);
//        return objectMapper.writeValueAsString(pushHistList);
//
//        PushHist pushHist = createPushHist(deviceType, deviceId, appCode, receiveSuccesYn, qryStartDt);
//        List<PushHist> pushHistList = pushManageDAO.getPushHistList(pushHist);
//        return objectMapper.writeValueAsString(pushHistList);
        return null;
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
//        PushHist criteria = PushHist.builder()
//                .sendSuccessYn(sendSuccessYn)
//                .sendDt(parsedSendDt)  // 여기서는 sendDt를 다시 파싱하지 않음
//                .build();

        return pushManageDAO.selectPushHistWithAssociations(criteria);
    }


//    @Override
//    public String getTargetResendListAsJson(String deviceType, String appCode, String notiCode, String custId, String deviceId, String qryStartDt) throws Exception {
//        PushHist pushHist = createPushHistForResend(deviceType, appCode, notiCode, custId, deviceId, qryStartDt);
//        List<PushHist> targetResendList = pushManageDAO.getTargetResendList(pushHist);
//        return objectMapper.writeValueAsString(targetResendList);
//    }

    @Override
    public String getPushNotiListAsJson(String appCode, String deviceType, String deviceId) throws Exception {
//        PushDevice pushDevice = createPushDevice(deviceType, deviceId, appCode, null,null);
//        List<PushNotiInfo> pushNotiInfoList = pushManageDAO.getPushNotiInfoList(pushDevice);
//        return objectMapper.writeValueAsString(pushNotiInfoList);
        return null;
    }

//    @Override
//    public String getPushDeviceListAsJson(String appCode, String deviceType, String custId) throws Exception {
//        PushDevice pushDevice = createPushDevice(deviceType, null, appCode, custId,null);
//        List<PushDevice> pushDeviceList = pushManageDAO.getPushDeviceList(pushDevice);
//        return objectMapper.writeValueAsString(pushDeviceList);
//    }

    @Override
    public String updateDeviceIds(String appCode, String deviceType, String oldDeviceId, String newDeviceId) throws Exception {
        TmpPushDevice tmpPushDevice = createTmpPushDevice(appCode, deviceType, oldDeviceId, newDeviceId);
        int pushResult = pushManageDAO.updatePushDevice(tmpPushDevice);
        if (pushResult > 0) {
            pushManageDAO.updatePushDeviceReg(tmpPushDevice);
            return "[SUCCESS]";
        }
        return "[FAIL]";
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

//    private PushHist createPushHist(Long pnrNo, String title, String message, LocalDateTime sendDt, String sendSuccessYn, String sendError) {
//        return PushHist.builder()
//                .pnrNo(pnrNo)
//                .title(title)
//                .message(message)
//                .sendDt(sendDt)
//                .sendSuccessYn(sendSuccessYn)
//                .sendError(sendError)
//                .build();
//    }


    private PushNotiReg createPushNotiReg(Long pnrNo, Long pdNo, Long pushInfoNo, LocalDateTime regDt) {
        return PushNotiReg.builder()
                .pnrNo(pnrNo)
                .pdNo(pdNo)
                .pushInfoNo(pushInfoNo)
                .regDt(regDt)
                .build();
    }

//    private PushHist createPushHist(String deviceType, String deviceId, String appCode, String receiveSuccesYn, String qryStartDt) {
//        LocalDateTime qryStartDateTime = (qryStartDt != null && !qryStartDt.isEmpty())
//                ? LocalDateTime.parse(qryStartDt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
//                : null;
//
//        return PushHist.builder()
//                .deviceType(deviceType)
//                .deviceId(deviceId)
//                .appCode(appCode)
//                .receiveSuccesYn(normalize(receiveSuccesYn))
//                .qryStartDt(qryStartDateTime)
//                .build();
//    }
//
//    private PushHist createPushHist(Long histNo, Long pnrNo, String title, String message, LocalDateTime qryStartDt, String sendSuccessYn, String sendError) {
//        LocalDateTime qryStartDateTime = (qryStartDt != null && !qryStartDt.isEmpty())
//                ? LocalDateTime.parse(qryStartDt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
//                : null;
//
//        return PushHist.builder()
//                .pnrNo(pnrNo)
//                .title(title)
//                .message(message)
//                .sendDt(sendDt)
//                .sendSuccessYn(sendSuccessYn)
//                .sendError(sendError)
//                .build();
//
//        return PushHist.builder()
//                .histNo(
//                .deviceType(deviceType)
//                .deviceId(deviceId)
//                .appCode(appCode)
//                .receiveSuccesYn(normalize(receiveSuccesYn))
//                .qryStartDt(qryStartDateTime)
//                .build();
//    }
//
//    private PushHist createPushHistForResend(String deviceType, String appCode, String notiCode, String custId, String deviceId, String qryStartDt) {
//        return PushHist.builder()
//                .deviceType(deviceType)
//                .appCode(normalize(appCode))
//                .notiCode(normalize(notiCode))
//                .custId(normalize(custId))
//                .deviceId(normalize(deviceId))
//                .qryStartDt(qryStartDt.isEmpty() ? null : LocalDateTime.parse(qryStartDt, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
//                .build();
//    }

    private TmpPushDevice createTmpPushDevice(String appCode, String deviceType, String oldDeviceId, String newDeviceId) {
        return TmpPushDevice.builder()
                .appCode(appCode)
                .deviceType(deviceType)
                .oldDeviceId(oldDeviceId)
                .newDeviceId(newDeviceId)
                .build();
    }

}
