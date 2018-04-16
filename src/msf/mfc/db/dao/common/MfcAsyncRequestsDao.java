
package msf.mfc.db.dao.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LeafNodeUpdateAction;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeUpdateRequestBody;
import msf.mfcfc.rest.common.JsonUtil;

public class MfcAsyncRequestsDao extends MfcAbstractCommonDao<MfcAsyncRequest, String> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAsyncRequestsDao.class);

  public List<MfcAsyncRequest> readListExecClusterInfo(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      List<MfcAsyncRequest> readListExecInfo = readListExecInfo(session);

      if (CollectionUtils.isEmpty(readListExecInfo)) {
        return new ArrayList<>();
      }

      List<MfcAsyncRequest> execClusterInfo = new ArrayList<>();
      for (MfcAsyncRequest mfcAsyncRequest : readListExecInfo) {
        switch (mfcAsyncRequest.getRequestMethodEnum()) {
          case POST:
            if (MfcFcRequestUri.SW_CLUSTER_CREATE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri()).matches()) {

              execClusterInfo.add(mfcAsyncRequest);
            }
            break;

          case DELETE:
            if (MfcFcRequestUri.SW_CLUSTER_DELETE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri()).matches()) {

              execClusterInfo.add(mfcAsyncRequest);
            }

            break;
          default:
            break;
        }
      }
      return execClusterInfo;
    } finally {
      logger.methodEnd();
    }
  }

  public List<MfcAsyncRequest> readListExecNodeInfo(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      List<MfcAsyncRequest> readListExecInfo = readListExecInfo(session);

      if (CollectionUtils.isEmpty(readListExecInfo)) {
        return new ArrayList<>();
      }

      List<MfcAsyncRequest> execNodeInfo = new ArrayList<>();
      for (MfcAsyncRequest mfcAsyncRequest : readListExecInfo) {
        switch (mfcAsyncRequest.getRequestMethodEnum()) {
          case POST:
            if (MfcFcRequestUri.LEAF_NODE_CREATE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri()).matches()) {

              execNodeInfo.add(mfcAsyncRequest);
            } else if (MfcFcRequestUri.SPINE_NODE_CREATE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri())
                .matches()) {

              execNodeInfo.add(mfcAsyncRequest);
            }
            break;

          case DELETE:
            if (MfcFcRequestUri.LEAF_NODE_DELETE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri()).matches()) {

              execNodeInfo.add(mfcAsyncRequest);
            } else if (MfcFcRequestUri.SPINE_NODE_DELETE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri())
                .matches()) {

              execNodeInfo.add(mfcAsyncRequest);
            }

            break;

          case PUT:
            if (MfcFcRequestUri.LEAF_NODE_UPDATE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri()).matches()) {

              LeafNodeUpdateRequestBody requestBody = JsonUtil.fromJson(mfcAsyncRequest.getRequestBody(),
                  LeafNodeUpdateRequestBody.class, ErrorCode.UNDEFINED_ERROR);

              if (LeafNodeUpdateAction.RECOVER_NODE.equals(requestBody.getActionEnum())) {

                execNodeInfo.add(mfcAsyncRequest);
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

  public List<MfcAsyncRequest> readListExecInfo(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcAsyncRequest.class)
          .add(Restrictions.or(Restrictions.eq("status", AsyncProcessStatus.WAITING.getCode()),
              Restrictions.eq("status", AsyncProcessStatus.RUNNING.getCode())));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<MfcAsyncRequest> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcAsyncRequest.class);
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
      String sql = "update MfcAsyncRequest set status = :afterStatus , subStatus = :afterSubStatus "
          + "where status = :beforeStatus";
      Query query = session.getSession().createQuery(sql).setParameter("beforeStatus", beforeStatus)
          .setParameter("afterStatus", afterStatus).setParameter("afterSubStatus", afterSubStatus);
      updateByQuery(session, query);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public MfcAsyncRequest read(SessionWrapper session, String operationId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcAsyncRequest.class)
        .add(Restrictions.eq("operationId", operationId));
    return readByCriteria(session, criteria);
  }

  public void delete(SessionWrapper session, Timestamp targetTime) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "targetTime" }, new Object[] { session, targetTime });
      Criteria criteria = session.getSession().createCriteria(MfcAsyncRequest.class)
          .add((Restrictions.lt("occurredTime", targetTime)));
      deleteByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }
}
