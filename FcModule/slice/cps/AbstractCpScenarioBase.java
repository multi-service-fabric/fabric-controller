package msf.fc.slice.cps;

import java.text.MessageFormat;
import java.util.Set;

import msf.fc.common.constant.CpUpdateAction;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.SliceType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.VpnType;
import msf.fc.common.data.CpId;
import msf.fc.common.data.CpIdPK;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.EdgePointPK;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.slices.CpIdDao;
import msf.fc.slice.AbstractSliceCpScenarioBase;

public abstract class AbstractCpScenarioBase<T extends RestRequestBase> extends AbstractSliceCpScenarioBase<T> {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractCpScenarioBase.class);

  protected void setRestIfTypeByAction(CpUpdateAction action) throws MsfException {
    try {
      logger.methodStart(new String[] { "action" }, new Object[] { action });
      switch (action) {
        case UPDATE:
          setRestIfType(SynchronousType.ASYNC);
          break;
        case RESERVE_CANCEL:
          setRestIfType(SynchronousType.SYNC);
          break;
        default:
          String logMsg = MessageFormat.format("undefined cp update action. action ={0}", action.name());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodeVpnType(Node node, VpnType vpnType) throws MsfException {
    try {
      logger.methodStart(new String[] { "node", "vpnType" }, new Object[] { node, vpnType });
      if (!node.getVpnTypeEnum().equals(vpnType)) {
        String logMsg = MessageFormat.format("node type not match. node.vpnType = {0}, compare vpnType = {1}",
            node.getVpnTypeEnum().name(), vpnType.name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected int getNextCpId(SessionWrapper sessionWrapper, Set<String> cpIdSet, String sliceId, SliceType sliceType)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "cpIdSet", "sliceId", "sliceType" },
          new Object[] { sessionWrapper, cpIdSet, sliceId, sliceType });
      CpIdDao cpIdDao = new CpIdDao();
      CpIdPK cpIdPk = new CpIdPK();
      cpIdPk.setLayerTypeEnum(sliceType);
      cpIdPk.setSliceId(sliceId);
      CpId cpId = cpIdDao.read(sessionWrapper, cpIdPk);

      int firstNextId = cpId.getNextId();
      int targetNextId = firstNextId;
      logger.performance("start get available cp id.");
      do {
        if (cpIdSet.contains(String.valueOf(targetNextId))) {
          targetNextId++;
          if (!checkCpIdRange(targetNextId)) {
            targetNextId = 1;
          }
        } else {
          updateCpId(sessionWrapper, cpIdDao, cpId, targetNextId + 1);
          logger.performance("end get available cp id.");
          return targetNextId;
        }
      } while (targetNextId != firstNextId);
      logger.performance("end get available cp id.");
      String logMsg = MessageFormat.format("threre is no available cp id. firstCheckId = {0}", firstNextId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  private void updateCpId(SessionWrapper sessionWrapper, CpIdDao cpIdDao, CpId cpId, int nextId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "cpIdDao", "cpId", "nextId" },
          new Object[] { sessionWrapper, cpIdDao, cpId, nextId });

      if (!checkCpIdRange(nextId)) {
        nextId = 1;
      }
      cpId.setNextId(nextId);
      cpIdDao.update(sessionWrapper, cpId);
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkCpIdRange(int checkTargetId) {
    try {
      logger.methodStart(new String[] { "checkTargetId" }, new Object[] { checkTargetId });
      if (checkTargetId >= 0 && checkTargetId < Integer.MAX_VALUE) {
        return true;
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected EdgePoint getEdgePointFromDb(SessionWrapper sessionWrapper, String swClusterId, int edgePointId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "swClusterId", "edgePointId" },
          new Object[] { sessionWrapper, swClusterId, edgePointId });
      EdgePointDao edgePointDao = new EdgePointDao();
      EdgePointPK pk = new EdgePointPK();
      pk.setSwClusterId(swClusterId);
      pk.setEdgePointId(edgePointId);
      return edgePointDao.read(sessionWrapper, pk);
    } finally {
      logger.methodEnd();
    }
  }
}
