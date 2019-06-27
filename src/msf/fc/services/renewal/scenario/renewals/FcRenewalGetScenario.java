
package msf.fc.services.renewal.scenario.renewals;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.services.renewal.scenario.renewals.data.RenewalReadResponseBody;
import msf.mfcfc.services.renewal.scenario.renewals.data.RenewalRequest;
import msf.mfcfc.services.renewal.scenario.renewals.data.entity.RenewalStatusEntity;

/**
 * Implementation class for the controller renewal state acquisition.
 *
 * @author NTT
 *
 */
public class FcRenewalGetScenario extends FcAbstractRenewalScenarioBase<RenewalRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcRenewalGetScenario.class);

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
   */
  public FcRenewalGetScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      return createResponse();
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(RenewalRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse() {
    try {
      logger.methodStart();
      List<RenewalStatusEntity> controllerRenewalStatusList = makeRenewalStatusEntityList();

      RenewalReadResponseBody responseBody = new RenewalReadResponseBody();
      responseBody.setControllerRenewalStatusList(controllerRenewalStatusList);

      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

}
