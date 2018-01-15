
package msf.fc.node.interfaces.physicalifs;

import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.node.interfaces.FcAbstractInterfaceScenarioBase;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.db.SessionWrapper;

/**
 * Abstract class to implement common process of physical interface-related
 * processing in configuration management function.
 *
 *
 * @author NTT
 *
 * @param <T>
 *          REST request base class
 */
public abstract class FcAbstractPhysicalInterfaceScenarioBase<T extends RestRequestBase>
    extends FcAbstractInterfaceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractPhysicalInterfaceScenarioBase.class);

  protected FcPhysicalIf getPhysicalInterface(SessionWrapper sessionWrapper, FcPhysicalIfDao fcPhysicalIfDao,
      Integer nodeType, Integer nodeId, String physicalIfId) throws MsfException {
    try {
      logger.methodStart();
      FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, nodeType, nodeId, physicalIfId);
      if (fcPhysicalIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = physicalIf");
      }
      return fcPhysicalIf;
    } finally {
      logger.methodEnd();
    }
  }
}
