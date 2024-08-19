package kr.wishtarot.mnspushapi.service.impl;

import kr.wishtarot.mnspushapi.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import kr.wishtarot.mnspushapi.dao.PushManageDAO;
import kr.wishtarot.mnspushapi.service.PushManageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public String isDeviceRegistered(String deviceType, String deviceId, String appCode) throws Exception {
        int count = countPushDeviceByCriteria(deviceType, deviceId, appCode);
        return (count > 0) ? "Y" : "N";
    }

    private int countPushDeviceByCriteria(String deviceType, String deviceId, String appCode) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, appCode, null);
        return pushManageDAO.countPushDeviceByCriteria(pushDevice);
    }

    private String getCustIdFromPushDevice(String deviceType, String deviceId, String appCode) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, appCode, null);
        return pushManageDAO.getCustIdFromPushDevice(pushDevice);
    }

    @Override
    public String manageDevice(String mode, String deviceType, String deviceId, String appCode, String custId) throws Exception {
        custId = normalize(custId);
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, appCode, custId);

        if ("reg".equalsIgnoreCase(mode)) {
            return handleDeviceRegistration(pushDevice);
        } else if ("del".equalsIgnoreCase(mode)) {
            return handleDeviceDeletion(pushDevice);
        } else {
            throw new IllegalArgumentException("[FAIL] Invalid mode");
        }
    }

    private String handleDeviceRegistration(PushDevice pushDevice) throws Exception {
        String regApp = pushManageDAO.getRegApp(pushDevice.getAppCode());
        // 등록된 앱이 아니면 등록 실패
        if (regApp != null) {
            int result = pushManageDAO.insertPushDevice(pushDevice);
            return (result > 0) ? "[SUCCESS]" : "[FAIL] Registration failed";
        }

        return "[FAIL] Registration failed";
    }

    private String handleDeviceDeletion(PushDevice pushDevice) throws Exception {
        int result = pushManageDAO.deletePushDevice(pushDevice);
        if (result > 0) {
            // 삭제 시 알림 수신 등록도 삭제
            pushManageDAO.deleteAllPushDeviceReg(pushDevice);
            return "[SUCCESS]";
        }
        return "[FAIL] Deletion failed";
    }

    @Override
    public String managePushNotification(String mode, String appCode, String notiCode, String deviceType, String deviceId) throws Exception {
        PushDeviceReg pushDeviceReg = createPushDeviceReg(appCode, notiCode, deviceType, deviceId);

        int count = countPushDeviceByCriteria(deviceType, deviceId, appCode);
        if(count == 0) {
            return "[FAIL] Device not registered";
        }

        int pushDeviceResult = 0;
        if ("reg".equalsIgnoreCase(mode)) {
            // 해당하는 알림이 등록되어 있는지 확인
            String regPushNoti = getRegPushNoti(appCode, notiCode);
            if (regPushNoti != null) {
                pushDeviceResult = pushManageDAO.insertPushDeviceReg(pushDeviceReg);
            }
        } else if ("del".equalsIgnoreCase(mode)) {
            pushDeviceResult = pushManageDAO.deletePushDeviceReg(pushDeviceReg);
        } else {
            throw new IllegalArgumentException("[FAIL] Invalid mode");
        }

        return (pushDeviceResult > 0) ? "[SUCCESS]" : "[FAIL]";
    }

    private String getRegPushNoti(String appCode, String notiCode) throws Exception {
        PushNotiInfo pushNotiInfo = createPushNotiInfo(appCode, notiCode);
        return pushManageDAO.getRegPushNoti(pushNotiInfo);
    }

    @Override
    public String getPushHistListAsJson(String deviceType, String deviceId, String appCode, String receiveSuccesYn, String qryStartDt) throws Exception {
        PushHist pushHist = createPushHist(deviceType, deviceId, appCode, receiveSuccesYn, qryStartDt);
        List<PushHist> pushHistList = pushManageDAO.getPushHistList(pushHist);
        return objectMapper.writeValueAsString(pushHistList);
    }

    @Override
    public String getTargetResendListAsJson(String deviceType, String appCode, String notiCode, String custId, String deviceId, String qryStartDt) throws Exception {
        PushHist pushHist = createPushHistForResend(deviceType, appCode, notiCode, custId, deviceId, qryStartDt);
        List<PushHist> targetResendList = pushManageDAO.getTargetResendList(pushHist);
        return objectMapper.writeValueAsString(targetResendList);
    }

    @Override
    public String getPushNotiListAsJson(String appCode, String deviceType, String deviceId) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, deviceId, appCode, null);
        List<PushNotiInfo> pushNotiInfoList = pushManageDAO.getPushNotiInfoList(pushDevice);
        return objectMapper.writeValueAsString(pushNotiInfoList);
    }

    @Override
    public String getPushDeviceListAsJson(String appCode, String deviceType, String custId) throws Exception {
        PushDevice pushDevice = createPushDevice(deviceType, null, appCode, custId);
        List<PushDevice> pushDeviceList = pushManageDAO.getPushDeviceList(pushDevice);
        return objectMapper.writeValueAsString(pushDeviceList);
    }

    @Override
    public String getCustIdListAsJson(String deviceType, String appCode, String notiCode) throws Exception {
        PushDeviceReg pushDeviceReg = createPushDeviceReg(appCode, notiCode, deviceType, null);
        List<PushDevice> custIdList = pushManageDAO.getCustIdList(pushDeviceReg);
        return objectMapper.writeValueAsString(custIdList);
    }

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

    private PushDevice createPushDevice(String deviceType, String deviceId, String appCode, String custId) {
        return PushDevice.builder()
                .deviceType(deviceType)
                .deviceId(deviceId)
                .appCode(appCode)
                .custId(custId)
                .build();
    }

    private PushDeviceReg createPushDeviceReg(String appCode, String notiCode, String deviceType, String deviceId) {
        return PushDeviceReg.builder()
                .appCode(appCode)
                .notiCode(notiCode)
                .deviceType(deviceType)
                .deviceId(deviceId)
                .build();
    }

    private PushNotiInfo createPushNotiInfo(String appCode, String notiCode) {
        return PushNotiInfo.builder()
                .appCode(appCode)
                .notiCode(notiCode)
                .build();
    }

    private PushHist createPushHist(String deviceType, String deviceId, String appCode, String receiveSuccesYn, String qryStartDt) {
        LocalDateTime qryStartDateTime = (qryStartDt != null && !qryStartDt.isEmpty())
                ? LocalDateTime.parse(qryStartDt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                : null;

        return PushHist.builder()
                .deviceType(deviceType)
                .deviceId(deviceId)
                .appCode(appCode)
                .receiveSuccesYn(normalize(receiveSuccesYn))
                .qryStartDt(qryStartDateTime)
                .build();
    }

    private PushHist createPushHistForResend(String deviceType, String appCode, String notiCode, String custId, String deviceId, String qryStartDt) {
        return PushHist.builder()
                .deviceType(deviceType)
                .appCode(normalize(appCode))
                .notiCode(normalize(notiCode))
                .custId(normalize(custId))
                .deviceId(normalize(deviceId))
                .qryStartDt(qryStartDt.isEmpty() ? null : LocalDateTime.parse(qryStartDt, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
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
