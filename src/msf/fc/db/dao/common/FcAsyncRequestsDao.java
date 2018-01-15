package msf.fc.db.dao.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;


public class FcAsyncRequestsDao extends FcAbstractCommonDao<FcAsyncRequest, String> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAsyncRequestsDao.class);


  
  public List<FcAsyncRequest> readListExecNodeInfo(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcAsyncRequest.class)
          .add(Restrictions.or(Restrictions.eq("status", AsyncProcessStatus.WAITING.getCode()),
              Restrictions.eq("status", AsyncProcessStatus.RUNNING.getCode())));
      List<FcAsyncRequest> readListExecInfo = readListByCriteria(session, criteria);

      if (CollectionUtils.isEmpty(readListExecInfo)) {
        return new ArrayList<>();
      }


      List<FcAsyncRequest> execNodeInfo = new ArrayList<>();
      for (FcAsyncRequest fcAsyncRequest : readListExecInfo) {
        switch (fcAsyncRequest.getRequestMethodEnum()) {
          case POST:
            if (MfcFcRequestUri.LEAF_NODE_CREATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri()).matches()) {

              execNodeInfo.add(fcAsyncRequest);
            } else if (MfcFcRequestUri.SPINE_NODE_CREATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri())
                .matches()) {

              execNodeInfo.add(fcAsyncRequest);
            }
            break;

          case DELETE:
            if (MfcFcRequestUri.LEAF_NODE_DELETE.getUriPattern().matcher(fcAsyncRequest.getRequestUri()).matches()) {

              execNodeInfo.add(fcAsyncRequest);
            } else if (MfcFcRequestUri.SPINE_NODE_DELETE.getUriPattern().matcher(fcAsyncRequest.getRequestUri())
                .matches()) {

              execNodeInfo.add(fcAsyncRequest);
            }

            break;
          default:
            break;
        }
      }
      return execNodeInfo;
    } finally {
      logger.methodEnd();
    }
  }

  
  public List<FcAsyncRequest> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcAsyncRequest.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  
  public void updateList(SessionWrapper session, Integer beforeStatus, Integer afterStatus, String afterSubStatus)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "beforeStatus", "afterStatus", "afterSubStatus" },
          new Object[] { session, beforeStatus, afterStatus, afterSubStatus });
      String sql = "update FcAsyncRequest set status = :afterStatus , subStatus = :afterSubStatus "
          + "where status = :beforeStatus";
      Query query = session.getSession().createQuery(sql).setParameter("beforeStatus", beforeStatus)
          .setParameter("afterStatus", afterStatus).setParameter("afterSubStatus", afterSubStatus);
      updateByQuery(session, query);
    } finally {
      logger.methodEnd();
    }
  }

  
  public void delete(SessionWrapper session, Timestamp targetTime) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "targetTime" }, new Object[] { session, targetTime });
      String sql = "delete FcAsyncRequest where occurredTime < :targetTime";
      Query query = session.getSession().createQuery(sql).setTimestamp("targetTime", targetTime);
      deleteByQuery(session, query);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcAsyncRequest read(SessionWrapper session, String operationId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcAsyncRequest.class)
        .add(Restrictions.eq("operationId", operationId));
    return readByCriteria(session, criteria);
  }
}
