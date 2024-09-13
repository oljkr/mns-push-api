package kr.wishtarot.mnspushapi.dao;

import java.util.List;
import java.util.Map;

import kr.wishtarot.mnspushapi.domain.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PushManageDAO {

    private final SqlSession sqlSession;

    @Autowired
    public PushManageDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Long getPdNoFromPushDevice(PushDevice pushDeviceVO) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getPdNoFromPushDevice", pushDeviceVO);
    }

    public List<Long> getPdNoListFromPushDevice(PushDevice pushDeviceVO) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getPdNoListFromPushDevice", pushDeviceVO);
    }

    public int countPushDeviceByCriteria(PushDevice pushDeviceVO) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.countPushDeviceByCriteria", pushDeviceVO);
    }

    public int countRegAppByCriteria(String appCode) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.countRegAppByCriteria", appCode);
    }

    public String getRegApp(String appCode) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getRegApp", appCode);
    }

    public Long getPushInfoNoByAppCodeAndNotiCode(Map<String, Object> params) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getPushInfoNoByAppCodeAndNotiCode", params);
    }

    public List<Long> getPushInfoNoListByAppCodeAndNotiCode(Map<String, Object> params) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getPushInfoNoListByAppCodeAndNotiCode", params);
    }

    public List<PushHist> getPushHistList(PushHist phv) {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getPushHistList", phv);
    }

    public List<PushHist> selectPushHistWithAssociations(PushHist criteria) {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.selectPushHistWithAssociations", criteria);
    }

    public List<AppPushInfo> getAppPushInfo() {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getAppPushInfo");
    }

    public List<PushHist> getTargetResendList(PushHist phv) {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getTargetResendList", phv);
    }

    public int insertPushDevice(PushDevice pdv) {
        return sqlSession.insert("kr.wishtarot.mnspushapi.mapper.PushManageMapper.insertPushDevice", pdv);
    }

    public int insertOrUpdatePushDevice(PushDevice pdv) {
        return sqlSession.insert("kr.wishtarot.mnspushapi.mapper.PushManageMapper.insertOrUpdatePushDevice", pdv);
    }

    public int updateDefaultNotiContent(PushDevice pdv) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.updateDefaultNotiContent", pdv);
    }

    public int updateMarketingNotiContent(PushDevice pdv) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.updateMarketingNotiContent", pdv);
    }

    public PushDevice getNotiConsentInfoList(PushDevice pdv){
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getNotiConsentInfoList", pdv);
    }

    public int updateCustIdByDeviceId(PushDevice pdv) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.updateCustIdByDeviceId", pdv);
    }

    public int deleteCustIdByDeviceId(PushDevice pdv) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.deleteCustIdByDeviceId", pdv);
    }

    public int insertDefaultNotification(Map<String, Object> params) {
        return sqlSession.insert("kr.wishtarot.mnspushapi.mapper.PushManageMapper.insertDefaultNotification", params);
    }

    public int insertDefaultMarketingNotification(Map<String, Object> params) {
        return sqlSession.insert("kr.wishtarot.mnspushapi.mapper.PushManageMapper.insertDefaultMarketingNotification", params);
    }

    public int insertPushNotiRegByDeviceAndCustIdAndAppCode(Map<String, Object> params) {
        return sqlSession.insert("kr.wishtarot.mnspushapi.mapper.PushManageMapper.insertPushNotiRegByDeviceAndCustIdAndAppCode", params);
    }

    public List<Long> selectPnrNoByDeviceAndCustIdAndAppCode(Map<String, Object> params){
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.selectPnrNoByDeviceAndCustIdAndAppCode", params);
    }

    public int deletePushNotiRegByPnrNoList(Map<String, Object> params){
        return sqlSession.delete("kr.wishtarot.mnspushapi.mapper.PushManageMapper.deletePushNotiRegByPnrNoList", params);
    }

    public int updateCustIdToNull(PushDevice pdv) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.updateCustIdToNull", pdv);
    }

    public int deletePushDevice(PushDevice pdv) {
        return sqlSession.delete("kr.wishtarot.mnspushapi.mapper.PushManageMapper.deletePushDevice", pdv);
    }

    public int insertPushNotiReg(PushNotiReg pnrv) {
        return sqlSession.insert("kr.wishtarot.mnspushapi.mapper.PushManageMapper.insertPushNotiReg", pnrv);
    }

    public int deletePushNotiReg(PushNotiReg pnrv) {
        return sqlSession.delete("kr.wishtarot.mnspushapi.mapper.PushManageMapper.deletePushNotiReg", pnrv);
    }

    public int deletePushNotiRegUsingPushDevice(PushDevice pdv) {
        return sqlSession.delete("kr.wishtarot.mnspushapi.mapper.PushManageMapper.deletePushNotiRegUsingPushDevice", pdv);
    }

    public int updateReceiveSuccess(long histSeq) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.updateReceiveSuccess", histSeq);
    }
}
