package kr.wishtarot.mnspushapi.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.wishtarot.mnspushapi.domain.PushDevice;
import kr.wishtarot.mnspushapi.domain.PushHist;
import kr.wishtarot.mnspushapi.domain.PushNotiInfo;
import kr.wishtarot.mnspushapi.domain.PushDeviceReg;
import kr.wishtarot.mnspushapi.domain.TmpPushDevice;

@Repository
public class PushManageDAO {

    private final SqlSession sqlSession;

    @Autowired
    public PushManageDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public String getCustIdFromPushDevice(PushDevice pushDeviceVO) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getCustIdFromPushDevice", pushDeviceVO);
    }

    public int countPushDeviceByCriteria(PushDevice pushDeviceVO) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.countPushDeviceByCriteria", pushDeviceVO);
    }

    public String getRegApp(String appCode) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getRegApp", appCode);
    }

    public String getRegPushNoti(PushNotiInfo pniv) {
        return sqlSession.selectOne("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getRegPushNoti", pniv);
    }

    public List<PushNotiInfo> getPushNotiInfoList(PushDevice pdv) {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getPushNotiInfoList", pdv);
    }

    public List<PushHist> getPushHistList(PushHist phv) {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getPushHistList", phv);
    }

    public List<PushHist> getTargetResendList(PushHist phv) {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getTargetResendList", phv);
    }

    public List<PushDevice> getCustIdList(PushDeviceReg pdrv) {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getCustIdList", pdrv);
    }

    public int insertPushDevice(PushDevice pdv) {
        return sqlSession.insert("kr.wishtarot.mnspushapi.mapper.PushManageMapper.insertPushDevice", pdv);
    }

    public int deletePushDevice(PushDevice pdv) {
        return sqlSession.delete("kr.wishtarot.mnspushapi.mapper.PushManageMapper.deletePushDevice", pdv);
    }

    public int insertPushDeviceReg(PushDeviceReg pdrv) {
        return sqlSession.insert("kr.wishtarot.mnspushapi.mapper.PushManageMapper.insertPushDeviceReg", pdrv);
    }

    public int deletePushDeviceReg(PushDeviceReg pdrv) {
        return sqlSession.delete("kr.wishtarot.mnspushapi.mapper.PushManageMapper.deletePushDeviceReg", pdrv);
    }

    public int deleteAllPushDeviceReg(PushDevice pdv) {
        return sqlSession.delete("kr.wishtarot.mnspushapi.mapper.PushManageMapper.deleteAllPushDeviceReg", pdv);
    }

    public List<PushDevice> getPushDeviceList(PushDevice pdv) {
        return sqlSession.selectList("kr.wishtarot.mnspushapi.mapper.PushManageMapper.getPushDeviceList", pdv);
    }

    public int updatePushDevice(TmpPushDevice tpdv) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.updatePushDevice", tpdv);
    }

    public int updatePushDeviceReg(TmpPushDevice tpdv) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.updatePushDeviceReg", tpdv);
    }

    public int updateReceiveSuccess(long histSeq) {
        return sqlSession.update("kr.wishtarot.mnspushapi.mapper.PushManageMapper.updateReceiveSuccess", histSeq);
    }
}
