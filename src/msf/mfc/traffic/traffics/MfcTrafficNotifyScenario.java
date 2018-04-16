
package msf.mfc.traffic.traffics;

import java.util.List;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.NoticeDestInfoTraffic;
import msf.mfc.common.config.type.system.Traffic;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.traffic.traffics.data.IfTrafficNotifyRequestBody;
import msf.mfcfc.traffic.traffics.data.TrafficNotifyRequest;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficClusterUnitEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficPhysicalUnitEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficSliceUnitEntity;

/**
 * Implementation class for the traffic information notification process.
 *
 * @author NTT
 *
 */
public class MfcTrafficNotifyScenario extends MfcAbstractTrafficNotifyScenarioBase<TrafficNotifyRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcTrafficNotifyScenario.class);

  private IfTrafficNotifyRequestBody requestBody;

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
  public MfcTrafficNotifyScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(TrafficNotifyRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNull(request.getRequestBody());
      IfTrafficNotifyRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          IfTrafficNotifyRequestBody.class);

      requestBody.validate();

      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;
    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {

    try {
      logger.methodStart();

      Traffic systemConfTraffic = MfcConfigManager.getInstance().getSystemConfTraffic();

      List<NoticeDestInfoTraffic> noticeDestInfoTrafficList = systemConfTraffic.getNoticeDestInfo();

      if (noticeDestInfoTrafficList.isEmpty()) {

        return createRestResponseBase();

      }

      for (NoticeDestInfoTraffic temp : noticeDestInfoTrafficList) {

        IfTrafficNotifyRequestBody higherOrderRequestBody = getIfTrafficNotifyRequestBodyData(requestBody, temp);

        if (!checkEmptyDataIfTrafficNotifyRequestBody(higherOrderRequestBody)) {

          sendTrafficNotify(higherOrderRequestBody, temp, systemConfTraffic.getNoticeRetryNum(),
              systemConfTraffic.getNoticeTimeout());
        }

      }

      return createRestResponseBase();
    } finally {
      logger.methodEnd();
    }

  }

  private boolean checkEmptyDataIfTrafficNotifyRequestBody(IfTrafficNotifyRequestBody body) {

    return checkDataSliceUnit(body.getSliceUnit()) && checkDataClusterUnit(body.getClusterUnit())
        && checkDataPhysicalUnit(body.getPhysicalUnit());
  }

  private boolean checkDataSliceUnit(IfTrafficSliceUnitEntity unit) {

    if (unit == null) {
      return true;
    }
    if (unit.getSliceList() == null) {
      return true;
    }
    if (unit.getSliceList().isEmpty()) {
      return true;
    }

    return false;

  }

  private boolean checkDataClusterUnit(IfTrafficClusterUnitEntity unit) {

    if (unit == null) {
      return true;
    }
    if (unit.getClusterList() == null) {
      return true;
    }
    if (unit.getClusterList().isEmpty()) {
      return true;
    }
    return false;

  }

  private boolean checkDataPhysicalUnit(IfTrafficPhysicalUnitEntity unit) {

    if (unit == null) {
      return true;
    }
    if (unit.getIfList() == null) {
      return true;
    }
    if (unit.getIfList().isEmpty()) {
      return true;
    }

    return false;

  }

  private IfTrafficNotifyRequestBody getIfTrafficNotifyRequestBodyData(IfTrafficNotifyRequestBody requestBody,
      NoticeDestInfoTraffic traffic) {

    try {
      logger.methodStart();
      IfTrafficNotifyRequestBody resultBody = new IfTrafficNotifyRequestBody();

      if (traffic.isIsPhysicalUnit()) {

        resultBody.setPhysicalUnit(requestBody.getPhysicalUnit());

      }

      if (traffic.isIsClusterUnit()) {

        resultBody.setClusterUnit(requestBody.getClusterUnit());

      }

      if (traffic.isIsSliceUnit()) {

        resultBody.setSliceUnit(requestBody.getSliceUnit());

      }
      return resultBody;

    } finally {
      logger.methodEnd();
    }
  }

}
