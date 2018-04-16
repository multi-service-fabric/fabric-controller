
package msf.mfc.failure.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.status.data.FailureStatusReadListResponseBody;
import msf.mfcfc.failure.status.data.FailureStatusRequest;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusIfFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusNodeFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusPhysicalUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;

/**
 * Implementation class for failure information list acquisition.
 *
 * @author NTT
 *
 */
public class MfcFailureStatusReadListScenario extends MfcAbstractFailureStatusScenarioBase<FailureStatusRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcFailureStatusReadListScenario.class);

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
  public MfcFailureStatusReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(FailureStatusRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {

    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();

      sessionWrapper.openSession();

      List<RestResponseData> restResponseDataList = sendFailureReadListRequestForFc(sessionWrapper);

      if (checkResponseAllSuccess(restResponseDataList)) {

        Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap = createFailureStatusFromFcMap(
            restResponseDataList);

        FailureStatusPhysicalUnitEntity failureStatusPhysicalUnitEntity = createAllPhysicalUnitFailureEntity(
            failureStatusFromFcMap);

        FailureStatusClusterUnitEntity failureStatusClusterUnitEntity = createAllClusterUnitEntity(
            failureStatusFromFcMap);

        FailureStatusSliceUnitEntity failureStatusSliceUnitEntity = createAllSliceNotifyEntity(sessionWrapper,
            failureStatusFromFcMap, null);

        FailureStatusReadListResponseBody failureStatusReadListResponseBody = new FailureStatusReadListResponseBody();
        failureStatusReadListResponseBody.setPhysicalUnit(failureStatusPhysicalUnitEntity);
        failureStatusReadListResponseBody.setClusterUnit(failureStatusClusterUnitEntity);
        failureStatusReadListResponseBody.setSliceUnit(failureStatusSliceUnitEntity);

        return new RestResponseBase(HttpStatus.OK_200, failureStatusReadListResponseBody);
      } else {

        return createErrorResponse(restResponseDataList, null);
      }
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private FailureStatusPhysicalUnitEntity createAllPhysicalUnitFailureEntity(
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap) {
    try {
      logger.methodStart();

      if (failureStatusFromFcMap == null || failureStatusFromFcMap.isEmpty()) {
        return null;
      }
      List<FailureStatusNodeFailureEntity> nodeList = new ArrayList<>();
      List<FailureStatusIfFailureEntity> ifList = new ArrayList<>();
      for (FailureStatusReadListResponseBody responseBody : failureStatusFromFcMap.values()) {

        if (responseBody.getPhysicalUnit() != null) {
          if (responseBody.getPhysicalUnit().getNodeList() != null) {
            nodeList.addAll(responseBody.getPhysicalUnit().getNodeList());
          }

          if (responseBody.getPhysicalUnit().getIfList() != null) {
            ifList.addAll(responseBody.getPhysicalUnit().getIfList());
          }
        }
      }
      FailureStatusPhysicalUnitEntity failureStatusPhysicalUnitEntity = new FailureStatusPhysicalUnitEntity();
      failureStatusPhysicalUnitEntity.setNodeList(nodeList);
      failureStatusPhysicalUnitEntity.setIfList(ifList);
      return failureStatusPhysicalUnitEntity;

    } finally {
      logger.methodEnd();
    }
  }

  private FailureStatusClusterUnitEntity createAllClusterUnitEntity(
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap) {
    try {
      logger.methodStart();
      if (failureStatusFromFcMap == null || failureStatusFromFcMap.isEmpty()) {
        return null;
      }
      List<FailureStatusClusterFailureEntity> clusterList = new ArrayList<>();
      for (FailureStatusReadListResponseBody responseBody : failureStatusFromFcMap.values()) {

        if (responseBody.getClusterUnit() != null && responseBody.getClusterUnit().getClusterList() != null) {
          clusterList.addAll(responseBody.getClusterUnit().getClusterList());
        }
      }
      FailureStatusClusterUnitEntity failureStatusClusterUnitEntity = new FailureStatusClusterUnitEntity();
      failureStatusClusterUnitEntity.setClusterList(clusterList);
      return failureStatusClusterUnitEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
