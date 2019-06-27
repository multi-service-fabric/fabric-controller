
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SpecialOperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.InternalNodeOsUpgradeRequest;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeInnerNotifyRequestBody;

/**
 * Implementation class of the node OS upgrade(for MSF internal interface).
 *
 * @author NTT
 *
 */
public class FcInternalNodeOsUpgradeScenario extends FcAbstractNodeOsUpgradeScenarioBase<InternalNodeOsUpgradeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalNodeOsUpgradeScenario.class);

  private InternalNodeOsUpgradeRequest request;
  private NodeOsUpgradeInnerNotifyRequestBody requestBody;

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
  public FcInternalNodeOsUpgradeScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
    this.specialOperationType = SpecialOperationType.SPECIALOPERATION;
  }

  @Override
  protected void checkParameter(InternalNodeOsUpgradeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      NodeOsUpgradeInnerNotifyRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          NodeOsUpgradeInnerNotifyRequestBody.class);

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

      RestResponseBase responseBase = null;

      FcInternalNodeOsUpgradeRunner upgradeRunner = new FcInternalNodeOsUpgradeRunner(request, requestBody);
      execAsyncRunner(upgradeRunner);

      responseBase = responseNodeOsUpgradeNotifyData();

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseNodeOsUpgradeNotifyData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
