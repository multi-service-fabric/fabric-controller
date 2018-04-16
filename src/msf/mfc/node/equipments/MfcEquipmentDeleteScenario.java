
package msf.mfc.node.equipments;

import java.util.List;

import msf.mfc.common.data.MfcEquipment;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcEquipmentDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.node.MfcNodeManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.equipments.data.EquipmentCreateRequestBody;
import msf.mfcfc.node.equipments.data.EquipmentReadResponseBody;
import msf.mfcfc.node.equipments.data.EquipmentRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for device model information deletion.
 *
 * @author NTT
 *
 */
public class MfcEquipmentDeleteScenario extends MfcAbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcEquipmentDeleteScenario.class);

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
  public MfcEquipmentDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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

      logger.performance("start wait to equipment increasing process.");
      synchronized (MfcNodeManager.getInstance().getMfcEquipmentCreateLockObject()) {
        logger.performance("end wait to equipment increasing process.");
        logger.performance("start wait to equipment decreasing process.");
        synchronized (MfcNodeManager.getInstance().getMfcEquipmentDeleteLockObject()) {
          logger.performance("end wait to equipment decreasing process.");

          RestResponseBase responseBase = null;
          SessionWrapper sessionWrapper = new SessionWrapper();
          try {
            sessionWrapper.openSession();
            sessionWrapper.beginTransaction();
            MfcEquipmentDao mfcEquipmentDao = new MfcEquipmentDao();

            MfcEquipment mfcEquipment = getEquipment(sessionWrapper, mfcEquipmentDao,
                Integer.valueOf(request.getEquipmentTypeId()));

            MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
            List<MfcSwCluster> mfcSwClusterList = getSwClusterList(sessionWrapper, mfcSwClusterDao);

            RestRequestBase responseBaseForRollback = new RestRequestBase();

            RestResponseBase sendEquipmentReadData = sendEquipmentRead(mfcSwClusterList.get(0),
                String.valueOf(mfcEquipment.getEquipmentTypeInfoId()));

            responseBaseForRollback.setRequestBody(setCreateEquipmentForRollback(sendEquipmentReadData));

            mfcEquipmentDao.delete(sessionWrapper, mfcEquipment.getEquipmentTypeInfoId());

            responseBase = sendEquipmentCreateDeleteRequest(sessionWrapper, mfcSwClusterList, false,
                responseBaseForRollback, String.valueOf(mfcEquipment.getEquipmentTypeInfoId()));

          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();
            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          return responseBase;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private String setCreateEquipmentForRollback(RestResponseBase sendEquipmentReadData) throws MsfException {
    try {
      logger.methodStart(new String[] { "sendEquipmentReadData" }, new Object[] { sendEquipmentReadData });
      EquipmentReadResponseBody equipmentReadResponseBody = JsonUtil.fromJson(sendEquipmentReadData.getResponseBody(),
          EquipmentReadResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
      EquipmentCreateRequestBody createRequestBody = new EquipmentCreateRequestBody();

      createRequestBody.setEquipmentType(equipmentReadResponseBody.getEquipmentType());
      return JsonUtil.toJson(createRequestBody);
    } finally {
      logger.methodEnd();
    }
  }

}
