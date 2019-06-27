
package msf.fc.db.dao.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LeafNodeUpdateAction;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeUpdateRequestBody;
import msf.mfcfc.rest.common.JsonUtil;

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

          case PUT:
            if (MfcFcRequestUri.LEAF_NODE_UPDATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri()).matches()) {

              LeafNodeUpdateRequestBody requestBody = JsonUtil.fromJson(fcAsyncRequest.getRequestBody(),
                  LeafNodeUpdateRequestBody.class, ErrorCode.UNDEFINED_ERROR);

              if (LeafNodeUpdateAction.RECOVER_NODE.equals(requestBody.getActionEnum())) {

                execNodeInfo.add(fcAsyncRequest);
              }
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
      if (AsyncProcessStatus.WAITING.getCode() == beforeStatus) {

        sql = sql + " and reservation_time IS NULL";
      }
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
      Criteria criteria = session.getSession().createCriteria(FcAsyncRequest.class)

          .add(Restrictions.lt("occurredTime", targetTime))

          .add(Restrictions.ge("status", AsyncProcessStatus.COMPLETED.getCode()));
      deleteByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcAsyncRequest> readListReservationInfo(SessionWrapper session, List<Pattern> uriPatternList)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "uriPatternList" }, new Object[] { session, uriPatternList });

      Criteria criteria = session.getSession().createCriteria(FcAsyncRequest.class)
          .add(Restrictions.eq("status", AsyncProcessStatus.WAITING.getCode()))
          .add(Restrictions.isNotNull("reservationTime"));

      List<FcAsyncRequest> readListReservationInfo = readListByCriteria(session, criteria);

      if (CollectionUtils.isEmpty(readListReservationInfo)) {
        return new ArrayList<>();
      }

      List<FcAsyncRequest> targetReservationInfo = new ArrayList<>();
      for (Pattern uriPattern : uriPatternList) {
        for (FcAsyncRequest fcReservationAsyncRequest : readListReservationInfo) {
          if (uriPattern.matcher(fcReservationAsyncRequest.getRequestUri()).matches()) {

            targetReservationInfo.add(fcReservationAsyncRequest);
          }
        }
      }

      if (CollectionUtils.isNotEmpty(targetReservationInfo)) {

        targetReservationInfo.sort(COMPARATOR_ASYNC_REQUEST_FOR_RESERVATION_TIME);
      }

      return targetReservationInfo;
    } finally {
      logger.methodEnd();
    }
  }

  private static final Comparator<FcAsyncRequest> COMPARATOR_ASYNC_REQUEST_FOR_RESERVATION_TIME = new Comparator<FcAsyncRequest>() {
    @Override
    public int compare(FcAsyncRequest o1, FcAsyncRequest o2) {
      return o1.getReservationTime().compareTo(o2.getReservationTime());
    }
  };

  public boolean hasNoRunningOperation(Map<String, Object> assignedOperationIdMap) throws MsfException {
    try {
      logger.methodStart(new String[] { "assignedOperationIdMap" }, new Object[] { assignedOperationIdMap });

      Set<String> operationIds = assignedOperationIdMap.keySet();
      if (CollectionUtils.isEmpty(operationIds)) {

        return true;
      }

      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        Criteria criteria = sessionWrapper.getSession().createCriteria(FcAsyncRequest.class)

            .add(Restrictions.in("operationId", operationIds))

            .add(Restrictions.or(Restrictions.isNull("reservationTime"),
                Restrictions.eq("status", AsyncProcessStatus.RUNNING.getCode())));

        List<FcAsyncRequest> readLisRunningOperationInfo = readListByCriteria(sessionWrapper, criteria);
        if (CollectionUtils.isNotEmpty(readLisRunningOperationInfo)) {

          return false;
        } else {

          return true;
        }
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }
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
