
package msf.fc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcNodeOperationInfo;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcNodeOperationInfoDao extends FcAbstractCommonDao<FcNodeOperationInfo, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeOperationInfoDao.class);

  public static synchronized boolean hasChangeNodeOperationStatus(Integer nodeOperationStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeOperationStatus" }, new Object[] { nodeOperationStatus });
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();
        FcNodeOperationInfoDao fcNodeOperationInfoDao = new FcNodeOperationInfoDao();
        List<FcNodeOperationInfo> fcNodeOperationInfos = fcNodeOperationInfoDao.readList(sessionWrapper);

        FcNodeOperationInfo fcNodeOperationInfo = new FcNodeOperationInfo();

        switch (fcNodeOperationInfos.get(0).getNodeOperationStatusEnum()) {
          case WAITING:
            if (NodeOperationStatus.WAITING.equals(NodeOperationStatus.getEnumFromCode(nodeOperationStatus))) {

              return false;
            } else if (NodeOperationStatus.RUNNING.equals(NodeOperationStatus.getEnumFromCode(nodeOperationStatus))) {

              fcNodeOperationInfo.setNodeOperationStatusEnum(NodeOperationStatus.RUNNING);
              fcNodeOperationInfoDao.create(sessionWrapper, fcNodeOperationInfo);
              fcNodeOperationInfoDao.delete(sessionWrapper, fcNodeOperationInfos.get(0).getNodeOperationStatus());
            } else {

              throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                  "Illegal parameter. nodeOperationStatusCode = " + nodeOperationStatus);
            }

            break;

          case RUNNING:
            if (NodeOperationStatus.WAITING.equals(NodeOperationStatus.getEnumFromCode(nodeOperationStatus))) {

              fcNodeOperationInfo.setNodeOperationStatusEnum(NodeOperationStatus.WAITING);
              fcNodeOperationInfoDao.create(sessionWrapper, fcNodeOperationInfo);
              fcNodeOperationInfoDao.delete(sessionWrapper, fcNodeOperationInfos.get(0).getNodeOperationStatus());
            } else if (NodeOperationStatus.RUNNING.equals(NodeOperationStatus.getEnumFromCode(nodeOperationStatus))) {

              return false;
            } else {

              throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                  "Illegal parameter. nodeOperationStatusCode = " + nodeOperationStatus);
            }
            break;

          default:

            throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Illegal parameter. currentNodeOperationStatusCode = "
                + fcNodeOperationInfos.get(0).getNodeOperationStatus());
        }
        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcNodeOperationInfo> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcNodeOperationInfo.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcNodeOperationInfo read(SessionWrapper session, Integer nodeOperationStatus) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcNodeOperationInfo.class)
        .add(Restrictions.eq("nodeOperationStatus", nodeOperationStatus));
    return readByCriteria(session, criteria);
  }
}
