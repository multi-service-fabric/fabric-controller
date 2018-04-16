
package msf.mfc.node.equipments;

import java.util.List;

import msf.mfc.common.data.MfcEquipment;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcEquipmentDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.node.MfcNodeManager;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.equipments.data.EquipmentCreateRequestBody;
import msf.mfcfc.node.equipments.data.EquipmentRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for device model information registration.
 *
 * @author NTT
 *
 */
public class MfcEquipmentCreateScenario extends MfcAbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentCreateRequestBody requestBody;

  private static final Integer FIRST_ID_NUM = 1;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcEquipmentCreateScenario.class);

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
  public MfcEquipmentCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EquipmentRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      EquipmentCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          EquipmentCreateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait to equipment increasing process.");
      synchronized (MfcNodeManager.getInstance().getMfcEquipmentCreateLockObject()) {
        logger.performance("end wait to equipment increasing process.");
        RestResponseBase responseBase = null;
        SessionWrapper sessionWrapper = new SessionWrapper();
        try {
          sessionWrapper.openSession();
          sessionWrapper.beginTransaction();

          MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
          List<MfcSwCluster> mfcSwClusterList = getSwClusterList(sessionWrapper, mfcSwClusterDao);

          MfcEquipmentDao mfcEquipmentDao = new MfcEquipmentDao();

          MfcEquipment mfcEquipment = setEquipmentData(sessionWrapper, mfcEquipmentDao);

          requestBody.getEquipmentType().setEquipmentTypeId(String.valueOf(mfcEquipment.getEquipmentTypeInfoId()));

          mfcEquipmentDao.create(sessionWrapper, mfcEquipment);

          RestRequestBase restRequest = new RestRequestBase();
          restRequest.setRequestBody(JsonUtil.toJson(requestBody));

          responseBase = sendEquipmentCreateDeleteRequest(sessionWrapper, mfcSwClusterList, true, restRequest,
              requestBody.getEquipmentType().getEquipmentTypeId());

        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          sessionWrapper.rollback();
          throw msfException;
        } finally {
          sessionWrapper.closeSession();
        }

        return responseBase;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcEquipment setEquipmentData(SessionWrapper sessionWrapper, MfcEquipmentDao mfcEquipmentDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "mfcEquipmentDao" },
          new Object[] { sessionWrapper, mfcEquipmentDao });
      MfcEquipment mfcEquipment = new MfcEquipment();

      mfcEquipment.setEquipmentTypeInfoId(getNextEquipmentTypeId(sessionWrapper, mfcEquipmentDao));

      return mfcEquipment;
    } finally {
      logger.methodEnd();
    }
  }

  protected Integer getNextEquipmentTypeId(SessionWrapper sessionWrapper, MfcEquipmentDao mfcEquipmentDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "equipmentDao" },
          new Object[] { sessionWrapper, mfcEquipmentDao });
      MfcEquipment biggestIdEquipment = mfcEquipmentDao.readFromBiggestId(sessionWrapper);
      if (null == biggestIdEquipment) {

        return FIRST_ID_NUM;
      } else {
        return biggestIdEquipment.getEquipmentTypeInfoId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }
}
