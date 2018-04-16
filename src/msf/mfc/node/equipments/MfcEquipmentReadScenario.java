
package msf.mfc.node.equipments;

import java.util.List;

import msf.mfc.common.data.MfcEquipment;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcEquipmentDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.equipments.data.EquipmentRequest;

/**
 * Implementation class for device model information acquisition.
 *
 * @author NTT
 *
 */
public class MfcEquipmentReadScenario extends MfcAbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcEquipmentReadScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public MfcEquipmentReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(EquipmentRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getEquipmentTypeId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        MfcEquipmentDao mfcEquipmentDao = new MfcEquipmentDao();

        MfcEquipment mfcEquipment = getEquipment(sessionWrapper, mfcEquipmentDao,
            Integer.parseInt(request.getEquipmentTypeId()));
        MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
        List<MfcSwCluster> mfcSwClusterList = getSwClusterList(sessionWrapper, mfcSwClusterDao);

        responseBase = sendEquipmentRead(mfcSwClusterList.get(0),
            String.valueOf(mfcEquipment.getEquipmentTypeInfoId()));
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

}
