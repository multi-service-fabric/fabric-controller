
package msf.fc.node.interfaces;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.AbstractInterfaceRunnerBase;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement the common process of interface-related
 * asynchronous processing in configuration management function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractInterfaceRunnerBase extends AbstractInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractInterfaceRunnerBase.class);

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcPhysicalIf checkPhisicalIf(SessionWrapper sessionWrapper, FcNode fcNode, String physicalIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "physicalIfId" }, new Object[] { fcNode, physicalIfId });
      FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
      FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, fcNode.getNodeType(), fcNode.getNodeId(),
          physicalIfId);
      if (fcPhysicalIf == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = physicalIf");
      }
      if ((!fcPhysicalIf.getClusterLinkIfs().isEmpty()) || (!fcPhysicalIf.getInternalLinkIfs().isEmpty())
          || (!fcPhysicalIf.getEdgePoints().isEmpty())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Specified physical If is used for other.");
      }
      return fcPhysicalIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcBreakoutIf checkBreakoutIf(SessionWrapper sessionWrapper, FcNode fcNode, String breakoutIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "breakoutIfId" }, new Object[] { fcNode, breakoutIfId });
      FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();
      FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, fcNode.getNodeType(), fcNode.getNodeId(),
          breakoutIfId);
      if (fcBreakoutIf == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = breakoutIf");
      }
      if ((!fcBreakoutIf.getClusterLinkIfs().isEmpty()) || (!fcBreakoutIf.getInternalLinkIfs().isEmpty())
          || (!fcBreakoutIf.getEdgePoints().isEmpty())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Specified breakout If is used for other.");
      }
      return fcBreakoutIf;
    } finally {
      logger.methodEnd();
    }
  }

}
