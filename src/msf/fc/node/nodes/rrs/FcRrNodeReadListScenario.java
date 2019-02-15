
package msf.fc.node.nodes.rrs;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.Rr;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.rrs.data.RrNodeReadDetailListResponseBody;
import msf.mfcfc.node.nodes.rrs.data.RrNodeReadListResponseBody;
import msf.mfcfc.node.nodes.rrs.data.RrNodeRequest;

/**
 * Implementation class for the RR node information list acquisition.
 *
 * @author NTT
 *
 */
public class FcRrNodeReadListScenario extends FcAbstractRrNodeScenarioBase<RrNodeRequest> {

  private RrNodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcRrNodeReadListScenario.class);

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
  public FcRrNodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(RrNodeRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

      checkUserTypeOperator(request.getUserTypeEnum());

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      FcConfigManager fcConfigManager = FcConfigManager.getInstance();

      List<Rr> rrs = fcConfigManager.getDataConfSwClusterData().getRrs().getRr();

      RestResponseBase responseBase = responseFcRrNodeReadListData(rrs, request.getFormat());

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseFcRrNodeReadListData(List<Rr> rrs, String format) {
    try {
      logger.methodStart(new String[] { "rrs", "format" }, new Object[] { rrs, format });
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        RrNodeReadDetailListResponseBody body = new RrNodeReadDetailListResponseBody();
        body.setRrList(getRrEntities(rrs));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        RrNodeReadListResponseBody body = new RrNodeReadListResponseBody();
        body.setRrNodeIds(getRrNodeIdList(rrs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
